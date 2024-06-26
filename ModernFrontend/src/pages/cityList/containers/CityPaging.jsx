import React, { useState, useEffect } from 'react';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import {useIntl} from "react-intl";

const headerStyle = {
    paddingTop: '10px',
};

function CityPaging({ onPageSizeChange }) {
    const {formatMessage} = useIntl();

    const [pageSizeInput, setPageSizeInput] = useState(() => {
        const storedPageSize = localStorage.getItem('pageSize');
        return storedPageSize ? storedPageSize : '';
    });

    const handlePageSizeChange = (event) => {
        setPageSizeInput(event.target.value);
    };

    const handlePageSizeSubmit = () => {
        if (pageSizeInput.trim() !== '') {
            onPageSizeChange(parseInt(pageSizeInput));
        }
    };

    handlePageSizeSubmit();

    return (
        <div style={headerStyle}>
            <Grid container spacing={1} alignItems="center">
                <Grid item>
                    <TextField
                        type="number"
                        label={formatMessage({id: 'pageSize'})}
                        variant="outlined"
                        value={pageSizeInput}
                        onChange={handlePageSizeChange}
                    />
                </Grid>
            </Grid>
        </div>
    );
}

export default CityPaging;