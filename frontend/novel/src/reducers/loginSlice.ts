import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import type { LoginResponse, LoginForm } from '../types/user';
import { postLogin } from '../apis/userApis';
import { getCookie, removeCookie, setCookie } from '../utils/cookieUtil';
// useState => useContext => react-redux

// store : 애플리케이션 내에 공유되는 상태 데이터
// reducer : 공유되는 상태 데이터를 처리 담당 함수

// slice : reducer + action(reducer 호출)

// 초기값 설정
const initialState: LoginResponse = {
  email: '',
  nickname: '',
  social: false,
  roles: [],
  accessToken: '',
};
// 비동기 호출
export const loginPostAsync = createAsyncThunk(
  'loginPostAsync',
  (param: LoginForm) => {
    return postLogin(param);
  },
);

// 쿠키 값 가져오기
const loadMemberCookie = () => {
  const member = getCookie('member');

  if (!member) return null;
  return member;
};

export const loginSlice = createSlice({
  name: 'auth',
  initialState: loadMemberCookie() || initialState,
  reducers: {
    login: (state, action) => {
      console.log('login');
      // loginParam 가져오기
      const { email } = action.payload;
      state.email = email;
    },
    logout: (state) => {
      console.log('logout');
      // 로그아웃 시 쿠키 삭제
      removeCookie('member');
      state.email = '';
    },
  },
  // 비동기 액션처리에 대한 상태 관리
  extraReducers: (builder) => {
    builder
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        console.log('fullfilled');
        state.email = action.payload.email;
        state.nickname = action.payload.nickname;
        state.social = action.payload.social;
        state.accessToken = action.payload.accessToken;
        state.roles = action.payload.roles;
        if (action.payload.accessToken) {
          setCookie('member', JSON.stringify(action.payload), 1);
        }
      })
      .addCase(loginPostAsync.pending, () => {
        console.log('pending');
      })
      .addCase(loginPostAsync.rejected, () => {
        console.log('rejected');
      });
  },
});
// 외부에서 사용할 수 있도록 함수(action) 내보내기
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;
