import styled from "styled-components";

const AddButton = styled.button`
  border: 1px solid;
  padding: 0;
  height: 23px;
  width: 23px;
  border-radius: 5px;
  box-sizing: border-box;

  &:hover {
    border: 1.5px solid;

    box-shadow: 0 0 20px #1d96967f;
  }
`;

export default AddButton;
