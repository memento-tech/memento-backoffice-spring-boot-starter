import { useSelector } from "react-redux";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import AddButton from "./AddButton";
import PlusIcon from "./icons/PlusIcon";
import CreationPopup from "../popup/CreationPopup";

const DataNav = () => {
  const navigate = useNavigate();
  const entityMetadatasState = useSelector(
    // @ts-ignore
    (state) => state.entityMetadatasState
  );

  const openCreationPopup = CreationPopup();

  return (
    <Container>
      <AddButton
        onClick={() =>
          openCreationPopup(
            entityMetadatasState.selectedEntityMetadata.entityName,
            () => navigate(0)
          )
        }
      >
        <PlusIcon height={30} />
      </AddButton>
      {entityMetadatasState.selectedEntityMetadata && (
        <CreateNewLabel>
          Create new {entityMetadatasState.selectedEntityMetadata.entityName}
        </CreateNewLabel>
      )}
    </Container>
  );
};

export default DataNav;

const Container = styled.div`
  margin: 10px 0 0 25px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
`;

const CreateNewLabel = styled.p`
  margin-left: 0.5rem;
`;
