export const formatDateTime = (isoString: string): string => {
    const date = new Date(isoString);
    const yyyy = date.getFullYear();
    const MM = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작
    const dd = String(date.getDate()).padStart(2, '0');
    return `${yyyy}.${MM}.${dd}`;
  };
  