import {useIntl} from 'react-intl';
import React, {useEffect, useState} from 'react';
import Typography from 'components/Typography';
import CityList from '../components/CityList';
import CitiesFilter from "./CitiesFilter";
import CityPaging from './CityPaging';
import CreateCityButton from "./CreateCityButton";
import {useNavigate} from "react-router-dom";

function CityListPage() {
    const {formatMessage} = useIntl();
    const navigate = useNavigate();
    const [filterCriteria, setFilterCriteria] = useState({
        id: '',
        cityName: '',
        countryId: '',
        cityPopulation: '',
        cityArea: '',
        foundedAt: '',
        languages: ''
    });
    const [pageSize, setPageSize] = useState(() => {
        const storedPageSize = localStorage.getItem('pageSize');
        return storedPageSize ? storedPageSize : 10
    });

    useEffect(() => {
        const storedFilterCriteria = window.localStorage.getItem('filterCriteria');
        if (storedFilterCriteria) {
            setFilterCriteria(JSON.parse(storedFilterCriteria));
        }

        setPageSize(JSON.parse(window.sessionStorage.getItem("pageSize")));
    }, []);

    useEffect(() => {
        window.localStorage.setItem('filterCriteria', JSON.stringify(filterCriteria));
    }, [filterCriteria]);

    const handleFilter = criteria => {
        setFilterCriteria(criteria);
    };

    const handleCreateCity = () => {
        window.localStorage.setItem('PassedCity', JSON.stringify({
            cityName: '',
            countryId: '',
            cityPopulation: '',
            cityArea: '',
            foundedAt: '',
            languages: ''
        }));
        navigate(`/cityDetails`);
    };

    const handlePageSizeChange = size => {
        window.localStorage.setItem('pageSize', size);
        setPageSize(size);
    };

    return (
        <div>
            <Typography>
                {formatMessage({id: 'title'})}
            </Typography>
            <CitiesFilter onFilter={handleFilter}/>
            <CreateCityButton onCreateCity={handleCreateCity}/>
            <CityPaging onPageSizeChange={handlePageSizeChange}/>
            <CityList filterCriteria={filterCriteria} pageSize={pageSize}
            />
        </div>
    );
}

export default CityListPage;