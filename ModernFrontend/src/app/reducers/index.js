import { combineReducers } from 'redux';

import user from './user';
import citiesReducer from "../../pages/cityList/reducers/citySlise";

export default combineReducers({
  user,
  cities: citiesReducer,
});
