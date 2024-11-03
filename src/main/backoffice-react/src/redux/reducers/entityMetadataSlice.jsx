import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import {
  getEntityOverview,
  refreshEntityMetadata,
} from "../../adapters/entityAdapter";

const initialState = {
  entityMetadatas: [],
  selectedEntityMetadata: null,
  loading: false,
  errorMessage: "",
};

const callEntityMetadataAPI = (isRefresh) => {
  if (isRefresh) {
    return refreshEntityMetadata();
  } else {
    return getEntityOverview();
  }
};

export const fetchEntityMetadatas = createAsyncThunk(
  "entity/fetchEntityMetadata",
  async (isRefresh, thunkAPI) => {
    const state = thunkAPI.getState();
    if (!isRefresh && state.entityMetadatas) {
      return {};
    }

    let response = await callEntityMetadataAPI(isRefresh);

    return {
      entityMetadatas: response.entityMetadata,
      errorMessage: response.exceptionMessage,
    };
  }
);

export const entityMetadataSlice = createSlice({
  name: "entityMetadatas",
  initialState,
  extraReducers: (builder) => {
    builder.addCase(fetchEntityMetadatas.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(fetchEntityMetadatas.fulfilled, (state, action) => {
      state.loading = false;
      if (action.payload.entityMetadatas) {
        state.entityMetadatas = action.payload.entityMetadatas;
        state.errorMessage = "";
      } else if (action.payload.errorMessage) {
        state.errorMessage = action.payload.errorMessage;
      }
    });
    builder.addCase(fetchEntityMetadatas.rejected, (state, action) => {
      state.loading = false;
      state.entityMetadatas = [];
      state.errorMessage = "Fetch is rejected";
    });
  },
  reducers: {
    setSelectedEntityMetadata: (state, action) => {
      state.selectedEntityMetadata = state.entityMetadatas.find(
        (entityMetadata) =>
          entityMetadata.entityName === action.payload.entityName
      );
    },
  },
});

export const { setSelectedEntityMetadata } = entityMetadataSlice.actions;

export default entityMetadataSlice.reducer;
