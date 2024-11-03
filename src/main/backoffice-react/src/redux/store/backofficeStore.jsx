import { configureStore } from "@reduxjs/toolkit";
import entityMetadatasReducer from "../reducers/entityMetadataSlice";
import entityDataReducer from "../reducers/entityDataSlice";

export const backofficeStore = configureStore({
  reducer: {
    entityMetadatasState: entityMetadatasReducer,
    entityDataState: entityDataReducer,
  },
});
