import type { ReactNode } from 'react';
import NavBar from '../components/common/Navbar';

function BasicLayout({ children }: { children: ReactNode }) {
  return (
    <>
      <div className="mx-auto h-screen max-w-160">
        <NavBar />
        <div className="h-[calc(100vh-56px)] overflow-y-scroll bg-white p-6">
          {children}
        </div>
      </div>
    </>
  );
}
export default BasicLayout;
