import React, { useState, useEffect } from 'react';
import Header from '@/components/Header';
import Icon from '@/components/Icon';
import { DayPicker, getDefaultClassNames } from 'react-day-picker';
import 'react-day-picker/style.css';
import { useThemeStore } from '@/store/themeStore';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import { Member } from '@/model/Member';
import CardCreationModal from '@/modal/CardCreationModal';

const CreateSharedCardPage: React.FC = () => {
  const [cardName, setCardName] = useState('');
  const [cardLimit, setCardLimit] = useState('');
  const [expireDate, setExpireDate] = useState('');
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  const [memberList, setMemberList] = useState<Member[]>([]);
  const [selectedMembers, setSelectedMembers] = useState<string[]>([]);
  const [showConfirmModal, setShowConfirmModal] = useState(false);

  const { walletId } = useParams<{ walletId: string }>();
  const navigate = useNavigate();
  const { theme } = useThemeStore();
  const base_url = process.env.REACT_APP_API_URL;

  const handleClearCardName = () => setCardName('');
  const handleClearCardLimit = () => setCardLimit('');
  const toggleCalendar = () => setIsCalendarOpen((prev) => !prev);

  const handleDateSelect = (date: string) => {
    setExpireDate(date);
    setIsCalendarOpen(false);
  };


  const toggleMemberSelection = (memberId: string) => {
    setSelectedMembers((prevSelected) =>
      prevSelected.includes(memberId)
        ? prevSelected.filter((id) => id !== memberId)
        : [...prevSelected, memberId]
    );
  };

  useEffect(() => {
    const fetchLinkMembers = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        if (!token) {
          console.error('Access token not found');
          return;
        }
        const response = await axios.get(
          `${base_url}/api/linked-wallets/${walletId}/members?size=100`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        const membersData = response.data.linkedMembers;
        const normalizedMembers = membersData.map((member: Member & { linkedMemberId?: string; name?: string }) => ({
          ...member,
          memberId: member.memberId || member.linkedMemberId || '',
          username: member.username || member.name || '',
        }));
        setMemberList(normalizedMembers);
        setSelectedMembers([]);
      } catch (error) {
        console.error('링크멤버 정보를 불러오는 중 오류 발생:', error);
      }
    };

    if (walletId) {
      fetchLinkMembers();
    }
  }, [base_url, walletId]);

  const confirmRegister  = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      console.error('Access token not found');
      return;
    }


    const limitPrice = Number(cardLimit.replace(/,/g, ''));


    const dateObj = new Date(expireDate);
    const year = dateObj.getFullYear();
    const month = dateObj.getMonth() + 1; 
    const day = dateObj.getDate();


  
    const payload = {
      cardName,
      limitPrice,
      expiredAt: [year, month, day],
      linkedMemberIds: selectedMembers,
      linkedWalletId: walletId,
    };

    console.log('payload:', payload);
    try {
      const response = await axios.post(`${base_url}/api/cards/shared`, payload, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
      });
      if (response.status === 201) {
        setShowConfirmModal(false);
        navigate(`/owned-linkwallet/${walletId}`);
      }
    } catch (error) {
      console.error('Failed to create shared card:', error);
    }
  };

  const isCardNameValid = cardName.length <= 10;
  const isFormComplete = cardName && cardLimit && expireDate && isCardNameValid;
  const defaultClassNames = getDefaultClassNames();

  const selectedMemberUsernames = memberList
  .filter((member) => selectedMembers.includes(member.memberId))
  .map((member) => member.username);

  const openConfirmModal = () => {
    if (isFormComplete) {
      setShowConfirmModal(true);
    }
  };
  
  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col flex-1 dark:bg-black">
      <Header headerType="back" onBackClick={() => navigate(-1)} />
      <div className="p-4 flex-1 space-y-8 mx-4 overflow-auto">
        {/* 카드 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">카드 이름</span>
          <div className="relative">
            <input
              type="text"
              placeholder="카드 이름을 입력하세요."
              value={cardName}
              onChange={(e) => setCardName(e.target.value)}
              className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-black dark:text-white dark:placeholder-white"
            />
            {cardName && (
              <button onClick={handleClearCardName} className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500">
                <Icon name={theme === 'dark' ? 'canceltextDarkIcon' : 'canceltextIcon'} width={24} height={24} alt="입력취소" />
              </button>
            )}
          </div>
          {cardName.length > 10 && <p className="mt-1 text-red-500 text-xs">카드 이름은 10자 내로 입력해 주세요.</p>}
        </div>

        {/* 카드 한도 입력 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">카드 한도(결제 가능 총액)</span>
          <div className="relative">
            <input
              type="text"
              placeholder="카드 한도를 입력하세요."
              value={cardLimit}
              onChange={(e) => {
                const Rvalue = e.target.value.replace(/,/g, '').replace(/\D/g, '');
                if (Rvalue === '') {
                  setCardLimit('');
                  return;
                }
                let num = Number(Rvalue);
                if (num > 10000000) num = 10000000;
                setCardLimit(num.toLocaleString());
              }}
              className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-black dark:text-white dark:placeholder-white"
            />
            {cardLimit && (
              <button onClick={handleClearCardLimit} className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500">
                <Icon name={theme === 'dark' ? 'canceltextDarkIcon' : 'canceltextIcon'} width={24} height={24} alt="입력취소" />
              </button>
            )}
          </div>
        </div>

        {/* 만료일 입력 */}
        <div className="relative">
          <span className="text-sm text-gray-600 dark:text-gray-400">만료일</span>
          <div className="relative flex items-center">
            <input
              type="text"
              placeholder="만료일을 선택하세요."
              value={expireDate}
              readOnly
              onClick={toggleCalendar}
              className="w-full py-2 pl-0 pr-10 border-b border-gray-300 focus:outline-none focus:ring-0 cursor-pointer dark:bg-black dark:text-white dark:placeholder-white"
            />
            <button onClick={toggleCalendar} className="absolute right-0 text-gray-500" style={{ anchorName: '--rdp' } as React.CSSProperties}>
              <Icon name={theme === 'dark' ? 'calendarDarkIcon' : 'calandarIcon'} width={24} height={24} alt="달력 아이콘" />
            </button>
          </div>

          {isCalendarOpen && (
            <div className="bg-white dark:bg-dark" style={{
                position: 'absolute',
                top: '100%', // 입력 필드 바로 아래에 나타나게 함
                right: 0,
                zIndex: 100, // 다른 요소 위에 표시할 수 있도록 충분히 높은 값
              }}>
              <DayPicker
                mode="single"
                selected={expireDate ? new Date(expireDate) : undefined}
                onSelect={(selectedDate: Date | undefined) => {
                  if (selectedDate) {
                    handleDateSelect(selectedDate.toLocaleDateString());
                  }
                }}
                classNames={{
                  today: 'bg-gray-300 text-white dark:bg-gray-400 rounded-lg',
                  selected: 'bg-indigo-500 border-amber-500 text-white rounded-lg',
                  root: `${defaultClassNames.root} shadow-lg p-5 dark:bg-[#010101] dark:text-white`,
                  chevron: `${defaultClassNames.chevron} dark:fill-white`,
                }}
              />
            </div>
          )}
        </div>

        {/* 멤버 초대 검색바 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">멤버 선택</span>
          <div className="mt-4 max-h-60 overflow-y-auto">
            <div className="mt-4 flex flex-wrap gap-2 justify-center">
              {memberList.map((member) => {
                const isSelected = selectedMembers.includes(member.memberId);
                return (
                  <div
                    key={member.memberId}
                    onClick={() => toggleMemberSelection(member.memberId)}
                    className={`cursor-pointer border rounded-full px-3 py-1 transition-colors w-60 text-center truncate ${
                      isSelected
                        ? 'bg-indigo-500 text-white border-transparent dark:bg-indigo-500 dark:text-white'
                        : 'bg-white text-[#010101] border-gray-300 dark:bg-[#9E9E9E] dark:text-[#FFFFFF]'
                    } `}
                  >
                    {member.username} ({member.email})
                  </div>
                );
              })}
            </div>
          </div>
          {selectedMembers.length === 0 && (
            <p className="mt-2 text-sm text-red-500">한명 이상의 멤버를 선택해 주세요.</p>
          )}
        </div>
      </div>

      <div className="p-4 mt-auto">
        <button
          disabled={!isFormComplete}
          onClick={openConfirmModal}
          className="font-bold block w-4/5 py-3 mx-auto bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
        >
          생성하기
        </button>
      </div>
      {showConfirmModal && (
        <CardCreationModal
          cardName={cardName}
          cardLimit={Number(cardLimit.replace(/,/g, ''))}
          expiryDate={expireDate}
          onClose={() => setShowConfirmModal(false)}
          onConfirm={confirmRegister}
        >
          <div className="mt-2">
            <p className="text-sm text-gray-700 dark:text-gray-300">
              {selectedMemberUsernames.join(', ')}
            </p>
          </div>
        </CardCreationModal>
      )}
    </div>
  );
};

export default CreateSharedCardPage;
