import { useState, useEffect } from "react";
import styled from "styled-components";
import ModalMessage from "../popup/ModalMessage";

const liftedLabelStyles = {
  paddingLeft: "1rem",
  fontSize: "0.9rem",
  transform: "translateY(-0.2rem)",
};

const BasicInputField = ({
  style = {},
  placeholder,
  value = null,
  errorMessage = "",
  passwordType = false,
  onChange,
  updatable = false,
  addMargin = true,
}) => {
  const [fieldValue, setFieldValue] = useState(value);
  const [isInputFocused, setIsInputFocused] = useState(false);
  const [hasContent, setHasContent] = useState(fieldValue?.length > 0);
  const [passwordVisible, setPasswordVisible] = useState(passwordType);

  useEffect(() => {
    setFieldValue(value);
    setHasContent(value?.length > 0);
  }, [value]);

  return (
    <InputFieldStyled style={{ margin: addMargin ? "0.5rem" : "0" }}>
      <LabelContainer>
        <InputLabel
          style={isInputFocused || hasContent ? liftedLabelStyles : {}}
        >
          {placeholder}
        </InputLabel>
      </LabelContainer>
      <InputContainer>
        <InputStyled
          style={style}
          autoComplete={passwordType ? "new-password" : "off"}
          type={passwordType ? (passwordVisible ? "password" : "text") : "text"}
          onChange={(e) => {
            setFieldValue(e.target.value);
            e.target.value ? setHasContent(true) : setHasContent(false);
            onChange(e.target.value);
          }}
          onFocus={() => setIsInputFocused(true)}
          onBlur={() => setIsInputFocused(false)}
          value={fieldValue ? fieldValue : ""}
          disabled={!updatable || placeholder === "updateAt"}
        />
        {passwordType && (
          <PassChangeIcon onClick={() => setPasswordVisible(!passwordVisible)}>
            {passwordVisible ? "show" : "hide"}
          </PassChangeIcon>
        )}
      </InputContainer>
      {errorMessage && (
        <ModalMessage
          messages={[errorMessage]}
          severity="error"
          width={"300px"}
        />
      )}
    </InputFieldStyled>
  );
};

export default BasicInputField;

const PassChangeIcon = styled.button`
  position: absolute;
  box-sizing: border-box;
  height: 26px;
  top: 13px;
  right: 5px;
  text-transform: uppercase;

  &:hover {
    color: ${(props) => props.theme.colors.primary};
    scale: 1.05;
  }
`;

const InputFieldStyled = styled.div`
  position: relative;
  height: 3.5rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  z-index: 1;

  width: 300px;
`;

const InputContainer = styled.div`
  width: 100%;
  height: 50%;
  display: flex;
  align-items: center;
`;

const InputStyled = styled.input`
  position: relative;
  width: 100%;

  font-size: 16px;
  border: 1px solid #bebdbd;
  border-radius: 5px;
  outline: none;
  background-color: transparent;
  padding-left: 6px;

  transition: border-color 0.3s ease;

  font-weight: 300;

  &:focus {
    border: 2px solid ${(props) => props.theme.colors.primary};
    padding-left: 5px;
  }
`;

const LabelContainer = styled.div`
  height: 20%;
`;

const InputLabel = styled.p`
  position: absolute;
  padding-left: 0.6rem;
  transform: translateY(16px);

  height: 1rem;
  color: #606160;
  z-index: -1;
  font-style: italic;
  font-size: 1.1rem;
`;
