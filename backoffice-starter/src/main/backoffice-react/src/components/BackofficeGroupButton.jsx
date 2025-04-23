import { useState } from "react";
import BackofficeNavButton from "./BackofficeNavButton";
import styled from "styled-components";
import ChevronUpIcon from "./icons/ChevronUpIcon";
import ChevronDownIcon from "./icons/ChevronDownIcon";

const BackofficeGroupButton = ({ group, index, handleEntityButtonClick }) => {
  const [open, setOpen] = useState(false);
  const [mouseOverSchevron, setMouseOverChevron] = useState(false);

  return (
    <>
      <BackofficeNavButton
        key={index}
        onClick={() => setOpen(!open)}
        title={group.groupTitle}
        className={open ? "active" : ""}
        onMouseOver={() => setMouseOverChevron(true)}
        onMouseOut={() => setMouseOverChevron(false)}
      >
        {group.groupTitle}
        {open ? (
          <ChevronUpIcon isHover={true} />
        ) : (
          <ChevronDownIcon isHover={mouseOverSchevron} />
        )}
      </BackofficeNavButton>
      {open && (
        <GroupContainer>
          {group.groupMetadata.map((entityMetadata) => (
            <BackofficeNavButton
              key={index}
              onClick={() => handleEntityButtonClick(entityMetadata.entityName)}
              title={entityMetadata.entityName}
            >
              {entityMetadata.entityTitle}{" "}
            </BackofficeNavButton>
          ))}
        </GroupContainer>
      )}
    </>
  );
};

export default BackofficeGroupButton;

const GroupContainer = styled.div`
  padding-left: 1rem;
`;
