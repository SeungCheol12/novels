import { useCallback, useEffect, useState } from 'react';
import { getList, putAvailable } from '../apis/novelApis';
import { initialPageState, type Novel, type PageResult } from '../types/book';

// 전체 목록 가져오기 hook
export const useNovles = (
  page: number,
  size: number,
  genre: number,
  keyword: string,
) => {
  const [serverData, setServerData] =
    useState<PageResult<Novel>>(initialPageState);
  const [loading, setLoading] = useState(false);
  const [error] = useState<unknown>(null);

  // 전체목록
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const data = await getList({ page: page + 1, size, genre, keyword });
      setServerData(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [page, size, genre, keyword]);

  // available 수정
  const toggleAvailable = useCallback(
    async (id: number, available: boolean) => {
      const result = await putAvailable({
        id: id,
        available: !available,
      });

      console.log('toggleAvailable', result);
      fetchData();
    },
    [fetchData],
  );

  useEffect(() => {
    // 랜더링 시 함수호출
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error, toggleAvailable };
};
