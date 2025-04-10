import React, { useEffect, useState } from 'react';
import Icon from '@/components/Icon';
import axios from 'axios';
import { Member } from '@/model/Member';
import { useThemeStore } from '@/store/themeStore';
import { IoSearch } from 'react-icons/io5';

const base_url = process.env.REACT_APP_API_URL;

interface MemberSearchBarProps {
  onMembersChange?: (members: Member[]) => void;
  initialMembers?: Member[];
}

const MemberSearchBar: React.FC<MemberSearchBarProps> = ({ onMembersChange, initialMembers = [] }) => {
  const [email, setEmail] = useState('');
  const [searchedEmails, setSearchedEmails] = useState([]);
  const [addedMembers, setAddedMembers] = useState<Member[]>([]);
  const [error, setError] = useState<string | null>(null);
  const { theme } = useThemeStore();
  const SearchIcon = IoSearch as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;

  const accessToken = sessionStorage.getItem('accessToken');
  useEffect(() => {
    if (!email) {
      setSearchedEmails([]);
      return;
    }

    const fetchEmails = async () => {
      try {
        const response = await axios.get(`${base_url}/api/members/search-email?keyword=${email}&size=5`, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`
          }
        });
        setSearchedEmails(response.data.emails || []);
      } catch (err) {
        console.error('자동완성 이메일 검색 실패', err);
        setSearchedEmails([]);
      }
    };

    const debounce = setTimeout(fetchEmails, 200); // ⏱️ 너무 잦은 호출 방지
    return () => clearTimeout(debounce);
  }, [email]);

  useEffect(() => {
    // memberId가 없으면 linkedmemberid를 사용하여 정규화
    const normalizedMembers = (initialMembers || []).map((member: Member & { linkedMemberId?: string }) => ({
      ...member,
      memberId: member.memberId || member.linkedMemberId || '',
      username: member.username,
      email: member.email
    }));
    setAddedMembers(normalizedMembers);
  }, [JSON.stringify(initialMembers)]);

  // addedMembers가 변경될 때 부모 컴포넌트에 알림
  useEffect(() => {
    // console.log('addedMembers changed:', addedMembers);
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

      const response = await axios.get(`${base_url}/api/members?email=${email}`, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`
        }
      });
      const member: Member | null = response.data; // 일치하는 사용자가 없으면 null 반환
      if (!member || !member.memberId) {
        // 검색 결과가 없을 경우 에러 메시지 표시
        setError('일치하는 사용자가 없습니다.');
      } else {
        // 이미 추가된 사용자가 아니라면 추가
        if (!addedMembers.find(m => m.memberId === member.memberId)) {
          setAddedMembers(prevMembers => [...prevMembers, member]);
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
    // console.log('Removing user with ID:', id);
    // console.log('Before removal:', addedMembers);
    setAddedMembers(prevMembers => {
      const updatedMembers = prevMembers.filter(member => member.memberId !== id);
      // console.log('After removal:', updatedMembers);
      return updatedMembers;
    });
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
          placeholder="회원 이메일 검색"
          className="w-full border-b border-gray-300 py-2 pr-10  focus:outline-none  dark:text-white dark:placeholder-white dark:bg-[#3b3838]"
        />
        <button
          onClick={handleSearch}
          className="absolute right-0 top-1/2 transform -translate-y-1/2 p-2"
        >
          <SearchIcon style={{
            width: '24px',
            height: '24px',
            color: theme == 'dark' ? 'rgb(107, 114, 128)' : 'rgb(107, 114, 128 )'
          }} />
        </button>
        {/* 🔽 자동완성 리스트 */}
        {searchedEmails.length > 0 && (
          <ul
            className="absolute left-0 w-full bg-white dark:bg-[#3b3838] border border-gray-300 z-10 rounded-md shadow-md mt-1 max-h-40 overflow-y-auto">
            {searchedEmails.map((item, idx) => (
              <li
                key={idx}
                onClick={() => setEmail(item)}
                className="px-4 py-2 cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-700 text-sm"
              >
                {item}
              </li>
            ))}
          </ul>
        )}
      </div>
      {/* 에러 메시지 */}
      {error && (
        <div className="mt-2 text-red-500">
          {error}
        </div>
      )}
      {/* 추가된 사용자 리스트 */}
      <div className="mt-4 flex flex-wrap gap-2 justify-center w-full">
        {addedMembers.map(member => (
          <div key={member.memberId}
               className="flex items-center justify-between border rounded-full px-3 py-1 min-w-[200px] dark:bg-[#9E9E9E] ">
            <div className="flex items-center w-60 text-center truncate">
              <span className="mr-2 dark:text-white">{member.username}({member.email})</span>
            </div>
            <button onClick={() => removeUser(member.memberId)}>
              <Icon name={theme === 'dark' ? 'searchcalcelDarkIcon' : 'searchcalcelIcon'}
                    width={theme === 'dark' ? 13 : 10} height={theme === 'dark' ? 13 : 10} alt="입력취소" />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MemberSearchBar;