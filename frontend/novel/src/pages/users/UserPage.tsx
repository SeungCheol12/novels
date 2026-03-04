import { Outlet } from 'react-router-dom';
import BasicLayout from '../../layouts/BasicLayout';

function UserPage() {
  return (
    <BasicLayout>
      <Outlet />
    </BasicLayout>
  );
}

export default UserPage;
