import React, { useState,useEffect } from 'react';
import Icon from '@/components/Icon';
import axios from 'axios';
import { Member } from '@/model/Member'
import { useThemeStore } from '@/store/themeStore';

const base_url = process.env.REACT_APP_API_URL;

interface MemberSearchBarProps {
    onMembersChange?: (members: Member[]) => void;
    initialMembers?: Member[];
}

const MemberSearchBar: React.FC<MemberSearchBarProps> = ({onMembersChange, initialMembers = []}) => {
  const [email, setEmail] = useState('');
  const [addedMembers, setAddedMembers] = useState<Member[]>(initialMembers);
  const [error, setError] = useState<string | null>(null);
  const { theme } = useThemeStore();

  useEffect(() => {
    setAddedMembers(initialMembers);
  }, []);

  useEffect(() => {
    if (onMembersChange) {
      onMembersChange(addedMembers);
    }
  }, [addedMembers, onMembersChange]);

  // 검색 실행: 이메일을 입력하고 검색 버튼 또는 엔터키로 API 요청을 보냅니다.
  const handleSearch = async () => {
    if (!email) return;
    try {
        const accessToken = sessionStorage.getItem('accessToken');
        if (!accessToken) {
        console.error('Access token not found');
        return;
        }
      
      const response = await axios.get(`${base_url}/api/members?email=${email}`,{
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          },
      });
      const member: Member | null = response.data; // 일치하는 사용자가 없으면 null 반환
      if (!member || !member.memberId) {
        // 검색 결과가 없을 경우 에러 메시지 표시 (모달로 대체 가능)
        setError('일치하는 사용자가 없습니다.');
      } else {
        // 이미 추가된 사용자가 아니라면 추가
        if (!addedMembers.find(m => m.memberId === member.memberId)) {
            setAddedMembers([...addedMembers, member]);
        }
        setEmail('');
        setError(null);
      }
    } catch (err) {
      console.error(err);
      setError('검색 중 오류가 발생했습니다.');
    }
  };

  // 엔터키 입력 시 검색 실행
  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  // 추가된 사용자 제거 함수
  const removeUser = (id: string) => {
    setAddedMembers(prevMembers => prevMembers.filter(member => member.memberId !== id));
  };

  return (
    <div>
      {/* 검색 입력창과 돋보기 아이콘 */}
      <div className="relative">
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          onKeyDown={handleKeyPress}
          placeholder="초대 대상 이메일 검색"
          className="w-full border-b border-gray-300 py-2 pr-10  focus:outline-none dark:bg-black dark:text-white dark:placeholder-white"
        />
        <button 
          onClick={handleSearch}
          className="absolute -right-3 top-1/2 transform -translate-y-1/2 p-2"
        >
          <Icon name={theme === 'dark' ? "searchDarkIcon" : "searchIcon"} width={50} height={50} alt="검색" />
        </button>
      </div>
      {/* 에러 메시지 (모달로 대체 가능) */}
      {error && (
        <div className="mt-2 text-red-500">
          {error}
        </div>
      )}
      {/* 추가된 사용자 리스트: 각 칩은 좌측에 이름, 우측에 x 아이콘 포함 */}
      <div className="mt-4 flex flex-wrap gap-2 justify-center w-full">
        {addedMembers.map(member => (
          <div key={member.memberId} className="flex items-center justify-between border rounded-full px-3 py-1 min-w-[200px] dark:bg-[#9E9E9E]">
            <span key={`span-${member.memberId}`} className="mr-2 dark:text-white">{member.username}({member.email})</span>
            <button key={`btn-${member.memberId}`} onClick={() => removeUser(member.memberId)}>
            <Icon key={`icon-${member.memberId}`} name={theme === 'dark' ? "searchcalcelDarkIcon" : "searchcalcelIcon"} width={theme === 'dark' ? 13 : 10} height={theme === 'dark' ? 13 : 10} alt="입력취소" />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MemberSearchBar;
