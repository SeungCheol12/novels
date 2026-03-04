// 서버 연동 담당

import axios from 'axios';
import type { Novel, NovelPut, PageRequestDTO } from '../types/book';
import jwtAxios from '../utils/jwtUtil';

export const API_SERVER_HOST = '/api/novels';

// 전체 목록 조회
export const getList = async (pageParam: PageRequestDTO) => {
  const { page, size, genre, keyword } = pageParam;

  const res = await axios.get(API_SERVER_HOST, {
    params: { page: page, size: size, genre: genre, keyword: keyword },
  });
  console.log('서버 도착', res);
  return res.data;
};
// 하나 조회
export const getRow = async (id: string) => {
  const res = await axios.get(`${API_SERVER_HOST}/${id}`);
  console.log('서버 도착', res);
  return res.data;
};

// available 업데이트
export const putAvailable = async (novelObj: NovelPut) => {
  const res = await jwtAxios.put(
    `${API_SERVER_HOST}/available/${novelObj.id}`,
    novelObj,
  );
  return res.data;
};
// available + genre 업데이트
export const putNovel = async (novelObj: NovelPut) => {
  const res = await jwtAxios.put(
    `${API_SERVER_HOST}/edit/${novelObj.id}`,
    novelObj,
  );
  return res.data;
};
// 삭제
export const deleteOne = async (id: number) => {
  const res = await jwtAxios.delete(`${API_SERVER_HOST}/${id}`);
  return res.data;
};
// 삽입
export const postNovel = async (novelObj: Novel) => {
  const res = await jwtAxios.post(`${API_SERVER_HOST}/add`, novelObj);
  return res.data;
};
// ai 소개글
export const getDesc = async (id: number) => {
  const res = await jwtAxios.get(`${API_SERVER_HOST}/${id}/ai-desc`);
  console.log('서버 도착', res);
  return res.data;
};
