export const addCity = city => {
    return {
        type: 'ADD_CITY',
        payload: city,
    };
};

export const deleteCity = cityId => {
    return {
        type: 'DELETE_CITY',
        payload: cityId,
    };
};

export const updateCity = city => {
    return {
        type: 'UPDATE_CITY',
        payload: city,
    };
};