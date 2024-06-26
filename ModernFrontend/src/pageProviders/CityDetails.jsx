import * as authorities from 'constants/authorities';
import CityDetailsPage from 'pages/cityDetail';
import React from 'react';

import PageAccessValidator from './components/PageAccessValidator';
import PageContainer from './components/PageContainer';

const CityList = (props) => {
    return (
        <PageAccessValidator
            neededAuthorities={[authorities.ENABLE_SEE_SECRET_PAGE]}
        >
            <PageContainer>
                <CityDetailsPage {...props} />
            </PageContainer>
        </PageAccessValidator>
    );
};

export default CityList;