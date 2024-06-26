import Button from '@mui/material/Button';
import {useIntl} from "react-intl";
import {useNavigate} from "react-router-dom";

const headerStyle = {
    paddingTop: '10px',
};

function CreateCityButton() {
    const navigate = useNavigate();
    const {formatMessage} = useIntl();

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

    return <div style={headerStyle}>
        <Button variant="contained" onClick={handleCreateCity}>
            {formatMessage({id: 'createCityButton'})}
        </Button>
    </div>
}

export default CreateCityButton;