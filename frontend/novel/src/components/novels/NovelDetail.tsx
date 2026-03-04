import { useNavigate } from 'react-router-dom';
import { deleteOne, getDesc } from '../../apis/novelApis';
import type { Novel } from '../../types/book';
import { getBookEmoji, renderStars } from '../../utils/novelUtil';
import { useState } from 'react';
import useLogin from '../../hooks/useLogin';
import Error from '../common/Error';

const NovelDetail = ({ novel }: { novel: Novel }) => {
  const navigate = useNavigate();

  // LLM -------------------- 모델 추가
  const [aiDescription, setAiDescription] = useState('');
  const [isGenerating, setIsGenerating] = useState(false);
  const [error, setError] = useState<unknown>(null);
  // 로그인정보
  const { authState } = useLogin();
  let roleName = '';
  if (authState.roles.includes('ADMIN')) roleName = 'ADMIN';
  const handleGenerate = async (id: number) => {
    try {
      setIsGenerating(true);
      // ai 소개글 도착
      const data = await getDesc(id);
      console.log('ai 소개글 ', data);
      setAiDescription(data.aiDesc);
    } catch (error) {
      setError(error);
    } finally {
      setIsGenerating(false);
    }
  };

  const description = novel.description?.trim()
    ? novel.description
    : aiDescription;
  // LLM -------------------- 모델 추가

  const deleteRow = async (id: number) => {
    try {
      const result = await deleteOne(id);
      console.log('삭제 후', result);
      // 삭제 후 페이지 이동
      navigate('/');
    } catch (error) {
      console.log(error);
    }
  };
  const handleDelete = (id: number) => {
    const confirmed = window.confirm('Delete this book?');
    if (!confirmed) return;
    deleteRow(id);
  };

  if (error) return <Error />;

  return (
    <>
      <section className="mt-6 flex border-t border-neutral-200 p-5">
        <div className="first:grow">
          <h2 className="mb-2.5 text-2xl">{novel.title}</h2>
          <p className="my-1.25">
            <strong>Author : {novel.author}</strong>
          </p>
          <p className="my-1.25">
            <strong>Genre : {novel.genreName}</strong>
          </p>
          <p className="my-1.25">
            <strong>Published Date : {novel.publishedDate}</strong>
          </p>
          <p className="my-1.25">
            <strong>Rating : {renderStars(novel.rating)}</strong>
            <span className="ml-1 text-[1.1em] tracking-widest text-sky-500"></span>
          </p>
          <p className="my-1.25">
            <strong>
              Available : {novel.available ? 'Available' : 'Not Available'}
            </strong>
          </p>
        </div>
        <div className="text-[8.6em]">{getBookEmoji(novel.id)}</div>
      </section>
      <section className="mt-6 border-t border-neutral-200 p-5">
        <p className="my-1.25">
          <strong>Summary</strong>
          <textarea
            name="summary"
            rows={5}
            className="w-full resize-none rounded-lg border border-gray-300 p-3 text-sm"
            defaultValue={novel.summary}
          />
        </p>
        {!isGenerating && description && (
          <p className="my-1.25">
            <strong>AI 소개글</strong>
            <textarea
              rows={6}
              className="w-full resize-none rounded-lg border border-gray-300 p-3 text-sm"
              defaultValue={description}
            />
          </p>
        )}
      </section>
      <section className="text-center">
        {!description && roleName === 'ADMIN' && (
          <button
            onClick={() => handleGenerate(novel.id)}
            className="mx-1 my-6 rounded-[5px] bg-orange-600 px-4 py-3 text-[1.2em] text-white hover:bg-sky-900"
          >
            AI 소개글 생성
          </button>
        )}
        <button
          onClick={() => navigate(`../edit/${novel.id}`)}
          className="mx-1 my-6 rounded-[5px] bg-sky-600 px-4 py-3 text-[1.2em] text-white hover:bg-sky-900"
        >
          Edit Book
        </button>
        <button
          onClick={() => handleDelete(novel.id)}
          className="mx-1 my-6 rounded-[5px] bg-red-600 px-4 py-3 text-[1.2em] text-white hover:bg-red-900"
        >
          Delete Book
        </button>
      </section>
    </>
  );
};

export default NovelDetail;
