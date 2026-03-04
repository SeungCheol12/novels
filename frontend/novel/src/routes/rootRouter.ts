import { createBrowserRouter } from 'react-router-dom';
import Home from '../components/novels/Home';
import { novelRouter } from './novelRouter';
import { userRouter } from './userRouter';
import UserPage from '../pages/users/UserPage';

const rootRouter = createBrowserRouter([
  {
    path: '/',
    Component: Home,
  },
  {
    path: '/novels',
    children: novelRouter(),
  },
  {
    path: '/member',
    Component: UserPage,
    children: userRouter(),
  },
]);

export default rootRouter;
