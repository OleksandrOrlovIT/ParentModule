import React, {useEffect, useState} from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import Pagination from '@mui/material/Pagination';
import {useNavigate} from "react-router-dom";
import {deleteCity, deleteCityAsync, fetchCityDataById} from "../../reducers/citySlise";
import {useDispatch, useSelector} from "react-redux";

export default function CityList(props) {
    const {filterCriteria, pageSize} = props;
    const dispatch = useDispatch();
    const citiesSlice = useSelector(state => state.cities.citiesSlice);
    const [checked, setChecked] = useState([]);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [selectedCity, setSelectedCity] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [deleteSuccess, setDeleteSuccess] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const storedPageSize = parseInt(window.localStorage.getItem('pageSize'), 10) || 10;
        setItemsPerPage(storedPageSize);

        const storedPageNumber = parseInt(window.localStorage.getItem('pageNumber'), 10) || 1;
        setPageNumber(storedPageNumber);
    }, []);

    useEffect(() => {
        window.localStorage.setItem('pageNumber', pageNumber.toString());
    }, [pageNumber]);

    useEffect(() => {
        setItemsPerPage(pageSize);
    }, [pageSize]);

    const handleDeleteDialogOpen = (city) => {
        setSelectedCity(city);
        setOpenDeleteDialog(true);
    };

    const handleDeleteDialogClose = () => {
        setOpenDeleteDialog(false);
    };

    const handleDelete = () => {
        dispatch(deleteCityAsync(selectedCity.id));
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }
        setOpenDeleteDialog(false);
        setDeleteSuccess(true);
        setTimeout(() => {
            setDeleteSuccess(false);
        }, 3000);
    };

    const filteredCities = citiesSlice.filter(city => {
        return Object.keys(filterCriteria).every(key => {
            if (!filterCriteria[key]) return true;
            return String(city[key]) === filterCriteria[key];
        });
    });

    const handleChangePage = (event, newPage) => {
        setPageNumber(newPage);
    };

    const startIndex = (pageNumber - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedCities = filteredCities.slice(startIndex, endIndex);

    async function handleCityOnClick(city) {
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            city = await fetchCityDataById(city);
        }
        window.localStorage.setItem("PassedCity", JSON.stringify(city));
        navigate(`/cityDetails`);
    }

    return (
        <div>
            <List sx={{width: '100%'}}>
                {paginatedCities.map((city, index) => {
                    const labelId = `checkbox-list-label-${index}`;

                    return (
                        <ListItem key={index} disablePadding>
                            <ListItemButton
                                role={undefined}
                                onClick={() => handleCityOnClick(city)}
                                onMouseEnter={() => setChecked([city.cityName])}
                                onMouseLeave={() => setChecked([])}
                                dense
                            >
                                <ListItemText id={labelId} primary={city.id}/>
                                <ListItemText id={labelId} primary={city.cityName}/>
                                <div onClick={(e) => e.stopPropagation()}>
                                    {checked.includes(city.cityName) && (
                                        <DeleteIcon
                                            onClick={() => handleDeleteDialogOpen(city)}
                                            color="error"
                                        />
                                    )}
                                </div>
                            </ListItemButton>
                        </ListItem>
                    );
                })}
            </List>
            <Pagination
                count={Math.ceil(filteredCities.length / itemsPerPage)}
                page={pageNumber}
                onChange={handleChangePage}
            />
            <Dialog open={openDeleteDialog} onClose={handleDeleteDialogClose}>
                <DialogTitle>Confirm Deletion</DialogTitle>
                <DialogContent>
                    Are you sure you want to delete city
                    with id = {selectedCity && selectedCity.id}
                    and name = {selectedCity && selectedCity.cityName}?
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDelete} color="error">
                        Delete
                    </Button>
                    <Button onClick={handleDeleteDialogClose} autoFocus>
                        Cancel
                    </Button>
                </DialogActions>
            </Dialog>
            {deleteSuccess && (
                <div>
                    <p>Element deleted successfully!</p>
                </div>
            )}
        </div>
    );
}