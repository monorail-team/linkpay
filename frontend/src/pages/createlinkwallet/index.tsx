import React, { useState } from 'react';
import Header from '@/components/Header';
import MemberSearchBar from '@/components/MemberSearchBar';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Member } from '@/model/Member';
import { MdOutlineCancel } from "react-icons/md";


const CreateLinkWalletPage: React.FC = () => {
  const [walletName, setWalletName] = useState('');
  const [selectedMembers, setSelectedMembers] = useState<Member[]>([]);

  const navigate = useNavigate();
  
  const CancelIcon = MdOutlineCancel as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
  const base_url = process.env.REACT_APP_API_URL;
  
  const handleClearLinkWalletName = () => setWalletName('');

  
  // 버튼 클릭
  const handleRegister = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      console.error('Access token not found');
      return;
    }

    const payload = {
        walletName,
        memberIds: selectedMembers.map(member => member.memberId)
    };

    console.log('payload:', payload);

    try {
      const response = await axios.post(`${base_url}/api/linked-wallets`, payload, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
      });
      if (response.status === 201) {
        navigate('/linkwalletlist');
      }
    } catch (error) {
      console.error('Failed to create linkwallet:', error);
    }
  };


  const isLinkWalletNameValid = walletName.length <= 10;
  const isFormComplete = walletName && selectedMembers && isLinkWalletNameValid;

  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col flex-1 dark:bg-[#3b3838]">
      <Header headerType="back" onBackClick={() => navigate("/linkwalletlist")} />
      <div className="p-4 flex-1 space-y-8 mx-4 overflow-auto"> 
        {/* 링크지갑 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600  dark:text-gray-400">링크 지갑 이름</span>
            <div className="relative">
                <input
                type="text"
                placeholder="링크지갑 이름을 입력하세요."
                value={walletName}
                onChange={(e) => setWalletName(e.target.value)}
                className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0  dark:text-white dark:placeholder-white dark:bg-[#3b3838]"
                />
                {walletName && (
                <button 
                    onClick={handleClearLinkWalletName}
                    className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                > 
                    <CancelIcon style={{width:"24px", height:"24px"}}/>
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
          {/* MemberSearchBar 컴포넌트에 onMembersChange prop을 전달하여 선택된 참여자를 상위 상태로 관리합니다. */}
          <MemberSearchBar onMembersChange={setSelectedMembers} />
        </div>
      </div>
      <div className="p-4 mt-auto">
        <button
          className="font-bold block w-4/5 py-3 mx-auto bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
          onClick={handleRegister}
          disabled={!isFormComplete}
        >
          생성하기
        </button>
      </div>
    </div>
  );
};

export default CreateLinkWalletPage;
