import styled from "styled-components";
import { useEffect } from "react";
import ConsoleNav from "../components/ConsoleNav";
import ConsoleData from "../components/ConsoleData";
import LoadingOverlay from "../components/LoadingOverlay";
import { useAlerts } from "../alert/AlertsContext";
import { useDispatch, useSelector } from "react-redux";
import { fetchEntityMetadatas } from "../redux/reducers/entityMetadataSlice";

const BackofficePage = () => {
  const { addAlert } = useAlerts();
  const dispatch = useDispatch();

  const entityMetadataState = useSelector(
    (state) => state.entityMetadatasState
  );

  useEffect(() => {
    if (entityMetadataState.errorMessages?.length > 0) {
      addAlert({
        severity: "error",
        messages: entityMetadataState.errorMessages,
        timeout: 2000,
      });
    }
  }, [entityMetadataState]);

  useEffect(() => {
    dispatch(fetchEntityMetadatas(false));
  }, []);

  return (
    <Container>
      {entityMetadataState.loading && <LoadingOverlay />}
      {!entityMetadataState.loading && (
        <>
          <ConsoleNav />
          <ConsoleData />
        </>
      )}
    </Container>
  );
};

export default BackofficePage;

const Container = styled.div`
  display: flex;
  background-color: #f0f0f0;
  min-width: 100vw;
`;
