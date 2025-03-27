// src/hooks/useDoubleBackExit.ts
import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const useDoubleBackExit = (setShowWarning: (val: boolean) => void) => {
  const backClickedRef = useRef(false);
  const timerRef = useRef<NodeJS.Timeout | null>(null);
  const navigate = useNavigate();

  useEffect(() => {

    const handlePopState = () => {
      if (backClickedRef.current) {
        // 두 번째 클릭 → 홈으로 이동
        navigate('/', { replace: true });
      } else {
        // 첫 번째 클릭 → 경고 표시
        backClickedRef.current = true;
        window.history.pushState(null, '', window.location.href);
        setShowWarning(true);
        console.log('true');


        // 1.5초 뒤 초기화
        timerRef.current = setTimeout(() => {
          backClickedRef.current = false;
          console.log('false');
          setShowWarning(false); // 🔹 상태 업데이트로 리렌더 유도
        }, 1500);
      }
    };
    window.history.pushState(null, '', window.location.href);
    window.history.replaceState(null, '', window.location.href);
    window.addEventListener('popstate', handlePopState);
    return () => {
      window.removeEventListener('popstate', handlePopState);
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, [navigate, setShowWarning]);
};

export default useDoubleBackExit;
