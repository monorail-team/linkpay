import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import ChargeModal from '../modal/ChargeModal';

describe('ChargeModal', () => {
  it('모달이 렌더링되는지 확인', () => {
    render(<ChargeModal onClose={jest.fn()} onConfirm={jest.fn()} />);
    expect(screen.getByText('충전할 금액을 입력해주세요.')).toBeInTheDocument();
  });

  it('입력 필드에 값을 입력하면 1000단위로 ,를 찍은 문자의 형태로 출력된다.', () => {
    render(<ChargeModal onClose={jest.fn()} onConfirm={jest.fn()} />);
    const input = screen.getByPlaceholderText('금액을 입력해주세요.');
    fireEvent.change(input, { target: { value: '5000' } });
    expect((input as HTMLInputElement).value).toBe('5,000');
  });

  it('확인 버튼을 클릭하면 onConfirm이 호출되는지 확인', () => {
    const mockConfirm = jest.fn();
    render(<ChargeModal onClose={jest.fn()} onConfirm={mockConfirm} />);
    const input = screen.getByPlaceholderText('금액을 입력해주세요.');
    fireEvent.change(input, { target: { value: '5000' } });

    fireEvent.click(screen.getByText('확인'));
    expect(mockConfirm).toHaveBeenCalledWith(5000);
  });

  it('취소 버튼을 클릭하면 onClose가 호출되는지 확인', () => {
    const mockClose = jest.fn();
    render(<ChargeModal onClose={mockClose} onConfirm={jest.fn()} />);

    fireEvent.click(screen.getByText('취소'));
    expect(mockClose).toHaveBeenCalled();
  });
});
