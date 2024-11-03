import styled from "styled-components";

const NonBasicButton = ({
  valueData,
  onClick,
  style = {},
  disabled = false,
}) => {
  return (
    <NonBasicButtonStyled onClick={onClick} style={style} disabled={disabled}>
      {valueData?.simpleName}
    </NonBasicButtonStyled>
  );
};

export default NonBasicButton;

const NonBasicButtonStyled = styled.button`
  padding-left: 6px;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  font-weight: 300;
  height: 23px;
  width: 100%;
  text-align: start;
  z-index: 5;

  border: 1px solid #bebdbd;
  border-radius: 5px;

  align-self: center;

  &:disabled {
    &:hover {
      text-shadow: unset;
      cursor: unset;
    }
  }

  &:hover {
    text-shadow: 0px 0px 1px black;
  }
`;
