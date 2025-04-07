import React, { useState, useEffect } from 'react';
import Header from '@/components/Header';
import Icon from '@/components/Icon';
import MemberSearchBar from '@/components/MemberSearchBar';
import { useThemeStore } from '@/store/themeStore';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import { Member } from '@/model/Member';

const ManageLinkWalletPage: React.FC = () => {
  const [walletName, setWalletName] = useState('');
  const [initialWalletName, setInitialWalletName] = useState('');
  const [selectedMembers, setSelectedMembers] = useState<Member[]>([]);
  const [initialMembers, setInitialMembers] = useState<Member[]>([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const { theme } = useThemeStore();
  const { walletId } = useParams<{ walletId: string }>();
  const base_url = process.env.REACT_APP_API_URL;

  // 링크지갑 단건 조회 API 호출
  useEffect(() => {
    const fetchLinkWallet = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const response = await axios.get(`${base_url}/api/linked-wallets/${walletId}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        const walletData = response.data;
        setWalletName(walletData.linkedWalletName);
        setInitialWalletName(walletData.linkedWalletName);
      } catch (error) {
        console.error('링크지갑 정보를 불러오는 중 오류 발생:', error);
      }
    };

    const fetchLinkMembers = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        console.log('walletid', walletId);
        const response = await axios.get(`${base_url}/api/linked-wallets/${walletId}/members?size=100`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        console.log(response.data);
        const membersData = response.data.linkedMembers;
        const myMemberEmail = sessionStorage.getItem('memberEmail');
        const filteredMembers = membersData
        .filter((member: Member & { linkedMemberId?: string }) => member.email !== myMemberEmail)
        .map((member: Member & { linkedMemberId?: string }) => ({
          ...member,
          memberId: member.memberId || member.linkedMemberId,
        }));
        console.log('filteredMembers', filteredMembers);
        setSelectedMembers(filteredMembers);
        setInitialMembers(filteredMembers);
      } catch (error) {
        console.error('링크멤버 정보를 불러오는 중 오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    if (walletId) {
      fetchLinkWallet();
      fetchLinkMembers();
    }
  }, [base_url, walletId]);
  
  // 데이터 변경 여부 확인 함수
  const isDataChanged = () => {
    const isWalletNameChanged = walletName !== initialWalletName;

    // 멤버 비교: memberId 리스트를 정렬하여 JSON 문자열로 비교
    const currentMemberIds = selectedMembers.map(m => m.memberId).sort();
    const initialMemberIds = initialMembers.map(m => m.memberId).sort();
    const isMembersChanged = JSON.stringify(currentMemberIds) !== JSON.stringify(initialMemberIds);

    return isWalletNameChanged || isMembersChanged;
  };

  const handleUpdate = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      console.error('Access token not found');
      return;
    }

    // 추가할 멤버: 현재 선택된 멤버에서 초기 목록에 없는 멤버
    const membersToAdd = selectedMembers.filter(
      member => !initialMembers.find(initial => initial.memberId === member.memberId)
    );
    // 제거할 멤버: 초기 목록에서 현재 선택된 멤버에 없는 멤버
    const membersToRemove = initialMembers.filter(
      member => !selectedMembers.find(selected => selected.memberId === member.memberId)
    );

    console.log("Members to add:", membersToAdd);
    console.log("Members to remove:", membersToRemove);
    
    try {
      for (const member of membersToAdd) {
        await axios.post(
          `${base_url}/api/linked-wallets/${walletId}/members`,
          { memberId: member.memberId },
          {
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
      }
      if (membersToRemove.length > 0) {
        if (membersToRemove.length === 1) {
          // 한 명 삭제: path parameter 사용
          await axios.delete(
            `${base_url}/api/linked-wallets/${walletId}/members/${membersToRemove[0].memberId}`,
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
              },
            }
          );
        } else {
          // 여러 명 삭제: query parameter 사용
          const memberIds = membersToRemove.map(member => member.memberId).join(',');
          await axios.delete(
            `${base_url}/api/linked-wallets/${walletId}/members?linkedMemberIds=${memberIds}`,
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
              },
            }
          );
        }
      }
      navigate(-1);
    } catch (error) {
      console.error('링크지갑 업데이트에 실패했습니다:', error);
      alert('업데이트에 실패했습니다.');
    }
  };

  const handleClearLinkWalletName = () => setWalletName('');

  const isLinkWalletNameValid = walletName.length <= 10;
  const isFormComplete = walletName && selectedMembers && isLinkWalletNameValid;

  if (loading) {
    return <p>로딩 중...</p>;
  }

  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col flex-1 dark:bg-black">
      <Header headerType="menu" onBackClick={() => navigate(-1)} />
      <div className="p-4 flex-1 space-y-8 mx-4 overflow-auto">
        {/* 링크지갑 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">링크 지갑 이름</span>
          <div className="relative">
            <input
              type="text"
              placeholder="링크지갑 이름을 입력하세요."
              value={walletName}
              onChange={(e) => setWalletName(e.target.value)}
              className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-black dark:text-white dark:placeholder-white"
            />
            {walletName && (
              <button
                onClick={handleClearLinkWalletName}
                className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
              >
                <Icon name={theme === 'dark' ? "canceltextDarkIcon" : "canceltextIcon"} width={24} height={24} alt="입력취소" />
              </button>
            )}
          </div>
          {walletName.length > 10 && (
            <p className="mt-1 text-red-500 text-xs">링크지갑 이름은 10자 내로 입력해 주세요.</p>
          )}
        </div>
        {/* 링크지갑 참여자 추가 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">링크 지갑 참여자 추가</span>
          {/* MemberSearchBar 컴포넌트에 onMembersChange prop을 전달하여 선택된 참여자를 관리 */}
          <MemberSearchBar onMembersChange={setSelectedMembers} initialMembers={initialMembers} />
        </div>
      </div>
      <div className="p-4 mt-auto">
        <button
          className="font-bold block w-4/5 py-3 mx-auto bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
          onClick={handleUpdate}
          disabled={!isFormComplete}
        >
          {isDataChanged() ? '수정하기' : '확인'}
        </button>
      </div>
    </div>
  );
};

export default ManageLinkWalletPage;
