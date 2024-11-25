import styled from "styled-components";

function Checkbox({ children, onChange, checked }) {
  return (
    <CheckboxContainer>
      <input type="checkbox" onChange={onChange} checked={checked} /> {children}
    </CheckboxContainer>
  );
}

export default Checkbox;

const CheckboxContainer = styled.div`
  display: flex;
`;
