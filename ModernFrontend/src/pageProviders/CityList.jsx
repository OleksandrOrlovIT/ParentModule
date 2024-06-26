import * as authorities from 'constants/authorities';
import CityListPage from 'pages/cityList';
import React from 'react';

import PageAccessValidator from './components/PageAccessValidator';
import PageContainer from './components/PageContainer';

const CityList = (props) => {
    return (
        <PageAccessValidator
            neededAuthorities={[authorities.ENABLE_SEE_SECRET_PAGE]}
        >
            <PageContainer>
                <CityListPage {...props} />
            </PageContainer>
        </PageAccessValidator>
    );
};

export default CityList;