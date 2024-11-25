import styled from "styled-components";

function ModalMessage({ messages, severity, width, italicText = false }) {
  return (
    <MessageContainer>
      {messages.map((message, index) => (
        <MessageText
          key={index}
          className={severity}
          style={{
            maxWidth: width,
            fontStyle: italicText ? "italic" : "normal",
          }}
        >
          {message}
        </MessageText>
      ))}
    </MessageContainer>
  );
}

export default ModalMessage;

const MessageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const MessageText = styled.p`
  font-size: ${(props) => props.theme.fonts.small};
  text-align: center;
  white-space: pre-wrap;

  &.error {
    color: ${(props) => props.theme.colors.error};
  }

  &.success {
    color: ${(props) => props.theme.colors.success};
  }

  &.info {
    color: ${(props) => props.theme.colors.info};
  }

  &.warning {
    color: ${(props) => props.theme.colors.warning};
  }
`;
