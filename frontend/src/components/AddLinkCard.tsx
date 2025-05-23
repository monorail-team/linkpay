import React from 'react';
import { GoPlus } from "react-icons/go";

interface AddLinkCardProps {
    onClick?: () => void;
  }
const AddLinkCard: React.FC<AddLinkCardProps> = ({ onClick }) => {

    const PlusIcon = GoPlus as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
  return (
    <div
        onClick={onClick}
        className="relative w-full max-w-[354px] mx-auto h-[181.5px] bg-[#938F99] rounded-xl shadow-md p-4 flex flex-col items-center justify-center">
    <PlusIcon className='text-[50px]' />
    {/* <div className="mt-2 text-center text-base text-white">링크 등록</div> */}
    </div>
  );
};

export default AddLinkCard;
