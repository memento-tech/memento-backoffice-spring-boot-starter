import { useEffect } from "react";
import styled from "styled-components";
import { OutlinedButton } from "../components/OutlinedButton";
import CloseIcon from "../components/icons/CloseIcon";

const Alert = ({
  message = "",
  messages = [],
  severity = "info",
  timeout = 0,
  handleDismiss = null,
  order,
}) => {
  useEffect(() => {
    if (timeout > 0 && handleDismiss) {
      const timer = setTimeout(() => {
        handleDismiss();
      }, timeout * 1000);
      return () => clearTimeout(timer);
    }
  }, []);

  const dismissAlert = (e) => {
    e.preventDefault();
    dismissAlert && handleDismiss();
  };

  return (
    (message?.length > 0 || messages?.length > 0) && (
      <AlertContainer className={severity} style={{ zIndex: 5000 + order }}>
        <CloseAlertIcon onClick={dismissAlert} className={severity}>
          <CloseIcon />
        </CloseAlertIcon>
        <AlertMessageContainer>
          {message && <AlertMessage>{message}</AlertMessage>}
          {messages &&
            messages.map((message, index) => (
              <AlertMessage key={index}>{message}</AlertMessage>
            ))}
        </AlertMessageContainer>
        <AlertCloseButton onClick={dismissAlert} className={severity}>
          Close
        </AlertCloseButton>
      </AlertContainer>
    )
  );
};

export default Alert;

const AlertContainer = styled.div`
  position: fixed;
  width: 40%;
  min-height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  left: 50%;
  transform: translateX(-50%) translateY(50px);
  z-index: 5000;
  border-radius: 5px;
  border: 1px solid;

  @media screen and (max-width: 1200px) {
    width: 60%;
  }

  @media screen and (max-width: 800px) {
    width: 80%;
  }

  animation: moveDown 0.5s linear;

  @keyframes moveDown {
    0% {
      transform: translateX(-50%) translateY(-100px);
    }
    100% {
      transform: translateX(-50%) translateY(50px);
    }
  }

  &.error {
    color: #842029;
    background-color: #fac2c7;
    border-color: #842029;
  }

  &.success {
    color: #256949;
    background-color: #bbffe1;
    border-color: #256949;
  }

  &.info {
    color: #03333d;
    background-color: #3dd8d8;
    border-color: #167486;
  }
`;

const CloseAlertIcon = styled.div`
  position: absolute;
  scale: 1.3;
  top: 0.5rem;
  right: 0.5rem;
  cursor: pointer;
  background-color: transparent;

  &:hover {
    scale: 1.5;
  }

  &.error {
    color: #842029;
  }

  &.success {
    color: #256949;
  }

  &.info {
    color: #03333d;
  }
`;

const AlertCloseButton = styled(OutlinedButton)`
  max-width: 40%;
  margin-bottom: 15px;
  border: 1px solid;

  &.error {
    color: #842029;
    background-color: transparent;
    border-color: #842029;

    &:hover {
      color: #ffffff;
      background-color: #842029;
      border-color: #ffffff;
    }
  }

  &.success {
    color: #256949;
    background-color: transparent;
    border-color: #256949;

    &:hover {
      color: #ffffff;
      background-color: #256949;
      border-color: #ffffff;
    }
  }

  &.info {
    color: #03333d;
    background-color: transparent;
    border-color: #03333d;

    &:hover {
      color: #ffffff;
      background-color: #03333d;
      border-color: #ffffff;
    }
  }
`;

const AlertMessageContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin: 2rem 0;
`;

const AlertMessage = styled.p`
  text-align: center;
`;
