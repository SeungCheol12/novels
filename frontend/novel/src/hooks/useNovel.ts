// id: string 으로 처리하는 이유는 주소줄에서 id 를 받아올 때는 string 형태

import { useCallback, useEffect, useState } from 'react';
import { getRow } from '../apis/novelApis';
import { initialNovel, type Novel } from '../types/book';

// spring server 단에서 형변환해서 받을 수 있기 때문에 상관없다
export const useNovel = (id?: string) => {
  const [serverData, setServerData] = useState<Novel>(initialNovel);
  const [loading, setLoading] = useState(false);
  const [error] = useState<unknown>(null);

  // 전체목록
  const fetchData = useCallback(async () => {
    if (!id) return;
    try {
      setLoading(true);
      const data = await getRow(id);
      setServerData(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [id]);
  useEffect(() => {
    // 랜더링 시 함수호출
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error };
};
