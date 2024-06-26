import {createSlice} from '@reduxjs/toolkit'
import staticCities from "../../../data/cities";
import axios from "axios";

const fetchCityDataById = async (city) => {
    try {
        const url = 'http://localhost:8080/api/city/' + city.id;
        const response = await axios.get(url);
        const {id, cityName, country: {id: countryId}, cityPopulation, cityArea, foundedAt, languages} = response;
        return {
            id,
            cityName,
            countryId,
            cityPopulation,
            cityArea,
            foundedAt,
            languages
        };
    } catch (error) {
        console.error("Error fetching city data:", error);
        return city;
    }
};

const fetchCityData = async () => {
    try {
        const response = await axios.post('http://localhost:8080/api/city/_list', {
            "page": 0,
            "size": 1000
        });
        window.localStorage.setItem("isConnectedToDB", "true");
        console.log("Sent a request = " + response.data.list);
        return response.data.list;
    } catch (error) {
        console.error("Error fetching city data:", error);
        window.localStorage.setItem("isConnectedToDB", "false");
        return staticCities;
    }
};

const initialState = {
    citiesSlice: await fetchCityData(),
};

let cityIdCounter = 101;

export const citiesSlice = createSlice({
    name: 'cities',
    initialState,
    reducers: {
        addCity: (state, action) => {
            action.payload.id = cityIdCounter;
            cityIdCounter += 1;
            state.citiesSlice.push(action.payload);
        },
        deleteCity: (state, action) => {
            state.citiesSlice = state.citiesSlice.filter(city => city.id !== action.payload);
        },
        updateCity: (state, action) => {
            const {id, updatedCity} = action.payload;
            const index = state.citiesSlice.findIndex(city => city.id === id);
            if (index !== -1) {
                state.citiesSlice[index] = {...state.citiesSlice[index], ...updatedCity};
            }
        },
    },
});

export const addCityAsync = (cityData) => async (dispatch) => {
    try {
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            const url = 'http://localhost:8080/api/city';
            const response = await axios.post(url, cityData);
            dispatch(addCity(response.data));
        } else {
            cityData.id = cityIdCounter;
            cityIdCounter += 1;
            dispatch(addCity(cityData));
        }
    } catch (error) {
        console.error("Error adding city:", error);
    }
};

export const deleteCityAsync = (cityId) => async (dispatch) => {
    try {
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            const url = `http://localhost:8080/api/city/${cityId}`;
            await axios.delete(url);
        } else {
            dispatch(deleteCity(cityId));
        }
    } catch (error) {
        console.error("Error deleting city:", error);
    }
};

export const updateCityAsync = (cityId, updatedCityData) => async (dispatch) => {
    try {
        console.log("updatedCityData = ", updatedCityData);
        if (window.localStorage.getItem("isConnectedToDB") === "true") {
            const url = `http://localhost:8080/api/city/${cityId}`;
            await axios.put(url, updatedCityData);
        } else {
            dispatch(updateCity({id: cityId, updatedCity: updatedCityData}));
        }
    } catch (error) {
        console.error("Error updating city:", error);
    }
};

// async function deleteCityFunc(state, action) {
//     try {
//         if (window.localStorage.getItem("isConnectedToDB") === "true") {
//             const url = 'http://localhost:8080/api/city/' + action.payload;
//             const response = await axios.delete(url);
//             console.log("response = ", response);
//         } else {
//             state.citiesSlice = state.citiesSlice.filter(city => city.id !== action.payload);
//         }
//     } catch (error) {
//         console.error("Error adding city:", error);
//     }
// }
//
// async function addCityFunc(state, action) {
//     try {
//         if (window.localStorage.getItem("isConnectedToDB") === "true") {
//             const url = 'http://localhost:8080/api/city';
//             const response = await axios.post(url, action.payload);
//             console.log("response = ", response);
//         } else {
//             action.payload.id = cityIdCounter;
//             cityIdCounter += 1;
//             state.citiesSlice.push(action.payload);
//         }
//     } catch (error) {
//         console.error("Error adding city:", error);
//     }
// }
//
// async function updateCityFunc(state, action) {
//     try {
//         if (window.localStorage.getItem("isConnectedToDB") === "true") {
//             const url = 'http://localhost:8080/api/city/' + action.payload.id;
//             const response = await axios.put(url, action.payload.updatedCity);
//             console.log("response = ", response);
//         } else {
//             const {id, updatedCity} = action.payload;
//             const index = state.citiesSlice.findIndex(city => city.id === id);
//             if (index !== -1) {
//                 state.citiesSlice[index] = {...state.citiesSlice[index], ...updatedCity};
//             }
//         }
//     } catch (error) {
//         console.error("Error adding city:", error);
//     }
// }

export const {
    addCity,
    deleteCity,
    updateCity
} = citiesSlice.actions;

export default citiesSlice.reducer;
export {fetchCityData};
export {fetchCityDataById};