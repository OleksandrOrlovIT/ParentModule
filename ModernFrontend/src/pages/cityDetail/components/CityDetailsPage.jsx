import { useIntl } from 'react-intl';
import React, {useEffect, useState} from 'react';
import { TextField, Button } from '@mui/material';
import {Link} from "react-router-dom";
import EditIcon from '@mui/icons-material/Edit';
import {useDispatch} from "react-redux";
import {updateCity, addCity, addCityAsync, updateCityAsync} from "../../cityList/reducers/citySlise";
import Typography from 'components/Typography';

function CityDetailsPage() {
    const {formatMessage} = useIntl();
    const [city, setCity] = useState(JSON.parse(window.localStorage.getItem("PassedCity")));
    const [editMode, setEditMode] = useState(false);
    const [editedCity, setEditedCity] = useState(city);
    const dispatch = useDispatch();
    const [errors, setErrors] = useState({
        cityName: '',
        countryId: '',
        cityPopulation: '',
        cityArea: '',
        foundedAt: '',
        languages: ''
    });

    useEffect(() => {
        checkErrors(true);
    }, [formatMessage]);


    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditedCity({ ...editedCity, [name]: value });
        setErrors({ ...errors, [name]: '' });
    };

    const handleEditClick = () => {
        setEditMode(true);
    };

    const checkErrors = (isValid) => {
        const newErrors = { ...errors };

        if (editedCity.cityName.trim() === '') {
            isValid = false;
            newErrors.cityName = formatMessage({id : 'cityNameError'});
        }

        if (isNaN(editedCity.countryId) || editedCity.countryId === '' || parseInt(editedCity.countryId) < 0) {
            isValid = false;
            newErrors.countryId = formatMessage({id : 'countryIdError'});
        }

        if (isNaN(editedCity.cityPopulation) || editedCity.cityPopulation === '' || parseInt(editedCity.cityPopulation) < 0) {
            isValid = false;
            newErrors.cityPopulation = formatMessage({id : 'cityPopulationError'});
        }

        if (isNaN(editedCity.cityArea) || editedCity.cityArea === '' || parseInt(editedCity.cityArea) < 0) {
            isValid = false;
            newErrors.cityArea = formatMessage({id : 'cityAreaError'});
        }

        if (isNaN(editedCity.foundedAt) || editedCity.foundedAt === '') {
            isValid = false;
            newErrors.foundedAt = formatMessage({id : 'foundedAtError'});
        }

        if (editedCity.languages.trim() === '') {
            isValid = false;
            newErrors.languages = formatMessage({id : 'languagesError'});
        }

        setErrors(newErrors);

        return isValid;
    }

    const handleSaveClick = () => {
        let isValid = true;

        isValid = checkErrors(isValid);

        if (!isValid) {
            return;
        }

        window.localStorage.setItem("PassedCity", JSON.stringify(editedCity));
        setCity(editedCity);
        setEditMode(false);
        if(editedCity.id === undefined){
            dispatch(addCityAsync(editedCity));
        } else {
            dispatch(updateCityAsync(editedCity.id, editedCity));
        }
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            setTimeout(() => {
                window.location.reload();
            }, 1000);
        }
    };

    const handleCancelClick = () => {
        setEditMode(false);
        setEditedCity(city);
        setErrors({
            cityName: '',
            countryId: '',
            cityPopulation: '',
            cityArea: '',
            foundedAt: '',
            languages: ''
        });
    };

    return (
        <div>
            <Typography color={"black"}>
                {formatMessage({id : 'title'})}
            </Typography>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                {!editMode && (
                    <Button variant="contained" onClick={handleEditClick} style={{marginRight: "0px", marginLeft: "auto"}}>
                        <EditIcon />
                    </Button>
                )}
                {editMode && (
                    <div>
                        <Button variant="contained" color="primary" onClick={handleSaveClick} style={{ marginRight: '5px' }}>
                            {formatMessage({id: 'saveButton'})}
                        </Button>
                        <Button variant="contained" color="secondary" onClick={handleCancelClick}>
                            {formatMessage({id : 'cancelButton'})}
                        </Button>
                    </div>
                )}
            </div>
            {!editMode ? (
                <div>
                    <TextField label={formatMessage({id : 'cityIdField'})}
                               value={city.id} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1, mt: 1}} />
                    <TextField label={formatMessage({id : 'cityNameField'})}
                               value={city.cityName} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                    <TextField label={formatMessage({id : 'countryIdField'})}
                                    value={city.countryId} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                    <TextField label={formatMessage({id : 'populationField'})}
                               value={city.cityPopulation} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                    <TextField label={formatMessage({id : 'areaField'})}
                               value={city.cityArea} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                    <TextField label={formatMessage({id : 'foundedAtField'})}
                               value={city.foundedAt} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                    <TextField label={formatMessage({id : 'languagesField'})}
                               value={city.languages} InputProps={{ readOnly: true }} fullWidth sx={{ mb: 1 }} />
                </div>
            ) : (
                <div>
                    <TextField label={formatMessage({id : 'cityNameField'})}
                               name="cityName" value={editedCity.cityName} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.cityName} helperText={errors.cityName} />
                    <TextField label={formatMessage({id : 'countryIdField'})}
                               name="countryId" value={editedCity.countryId} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.countryId} helperText={errors.countryId} />
                    <TextField label={formatMessage({id : 'populationField'})}
                               name="cityPopulation" value={editedCity.cityPopulation} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.cityPopulation} helperText={errors.cityPopulation} />
                    <TextField  label={formatMessage({id : 'areaField'})}
                                name="cityArea" value={editedCity.cityArea} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.cityArea} helperText={errors.cityArea} />
                    <TextField label={formatMessage({id : 'foundedAtField'})}
                               name="foundedAt" value={editedCity.foundedAt} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.foundedAt} helperText={errors.foundedAt} />
                    <TextField label={formatMessage({id : 'languagesField'})}
                               name="languages" value={editedCity.languages} onChange={handleInputChange} fullWidth sx={{ mb: 1 }} error={!!errors.languages} helperText={errors.languages} />
                </div>
            )}
            <Button variant="contained" color="secondary" component={Link} to={`/cityList`} sx={{ mt: 1 }}>
                {formatMessage({id : 'returnButton'})}
            </Button>
        </div>
    );
}

export default CityDetailsPage;