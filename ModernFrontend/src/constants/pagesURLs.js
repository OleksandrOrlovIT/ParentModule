import * as pages from './pages';
import config from 'config';

const result = {
  [pages.defaultPage]: `${config.UI_URL_PREFIX}/${pages.defaultPage}`,
  [pages.login]: `${config.UI_URL_PREFIX}/${pages.login}`,
  [pages.secretPage]: `${config.UI_URL_PREFIX}/${pages.secretPage}`,
  [pages.cityListPage]: `${config.UI_URL_PREFIX}/${pages.cityListPage}`,
  [pages.cityDetailsPage]: `${config.UI_URL_PREFIX}/${pages.cityDetailsPage}`,
};

export default result;
