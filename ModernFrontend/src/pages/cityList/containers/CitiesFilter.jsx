import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import {useIntl} from "react-intl";

export default function CitiesFilter(props) {
    const {formatMessage} = useIntl();
    const { onFilter } = props;
    const [filterCriteria, setFilterCriteria] = useState(() => {
        const storedCriteria = localStorage.getItem('filterCriteria');
        return storedCriteria ? JSON.parse(storedCriteria) : {
            id: '',
            cityName: '',
            countryId: '',
            cityPopulation: '',
            cityArea: '',
            foundedAt: '',
            languages: ''
        };
    });

    const handleChange = (event, field) => {
        const value = event.target.value;
        setFilterCriteria(prevState => ({
            ...prevState,
            [field]: value
        }));
    };

    const handleFilter = () => {
        onFilter(filterCriteria);
    };

    useEffect(() => {
        localStorage.setItem('filterCriteria', JSON.stringify(filterCriteria));
    }, [filterCriteria]);

    return (
        <div>
            <Button onClick={handleFilter} variant="contained" color="primary">
                {formatMessage({id: 'filterButton'})}
            </Button>
            <div style={{marginTop: '10px'}}>
                    <TextField
                        label={formatMessage({id: 'idField'})}
                        variant="outlined"
                        value={filterCriteria.id}
                        onChange={(e) => handleChange(e, 'id')}
                    />
                    <TextField
                        label={formatMessage({id: 'cityNameField'})}
                        variant="outlined"
                        value={filterCriteria.cityName}
                        onChange={(e) => handleChange(e, 'cityName')}
                    />
                    <TextField
                        label={formatMessage({id: 'countryIdField'})}
                        variant="outlined"
                        value={filterCriteria.countryId}
                        onChange={(e) => handleChange(e, 'countryId')}
                    />
                    <TextField
                        label={formatMessage({id: 'cityPopulationField'})}
                        variant="outlined"
                        value={filterCriteria.cityPopulation}
                        onChange={(e) => handleChange(e, 'cityPopulation')}
                    />
                    <TextField
                        label={formatMessage({id: 'cityAreaField'})}
                        variant="outlined"
                        value={filterCriteria.cityArea}
                        onChange={(e) => handleChange(e, 'cityArea')}
                    />
                    <TextField
                        label={formatMessage({id: 'foundedAtField'})}
                        variant="outlined"
                        value={filterCriteria.foundedAt}
                        onChange={(e) => handleChange(e, 'foundedAt')}
                    />
                    <TextField
                        label={formatMessage({id: 'languagesField'})}
                        variant="outlined"
                        value={filterCriteria.languages}
                        onChange={(e) => handleChange(e, 'languages')}
                    />
            </div>
        </div>
    );
}
