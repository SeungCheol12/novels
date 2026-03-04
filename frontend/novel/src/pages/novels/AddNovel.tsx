import { useNavigate } from 'react-router-dom';
import NovelForm from '../../components/novels/NovelForm';
import BasicLayout from '../../layouts/BasicLayout';
import { initialNovel, type Novel } from '../../types/book';
import { postNovel } from '../../apis/novelApis';
import useLogin from '../../hooks/useLogin';

const AddBook = () => {
  const navigate = useNavigate();
  const { isLogin } = useLogin();
  const handleCancel = (id: number) => {
    // 이전 페이지로 이동
    history.back();
    console.log(id);
  };
  const handleSubmit = async (formData: Novel) => {
    // 서버로 업데이트 요청
    try {
      const id = await postNovel(formData);
      console.log('수정 후 ', id);
      // 상세보기
      navigate(`../${id}`);
    } catch (error) {
      console.log(error);
    }
  };
  // 로그인 여부 확인
  if (!isLogin) {
    // 로그인 페이지 이동
    navigate('/member/login');
  }
  return (
    <BasicLayout>
      <h1 className="text-[32px]">Add New Book</h1>
      <NovelForm
        novel={initialNovel}
        onCancel={handleCancel}
        onSubmit={handleSubmit}
      />
    </BasicLayout>
  );
};

export default AddBook;
