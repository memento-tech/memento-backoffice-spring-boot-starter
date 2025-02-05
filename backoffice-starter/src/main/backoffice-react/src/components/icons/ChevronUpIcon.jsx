import { useEffect, useState } from "react";

const ChevronUpIcon = ({
  height = 20,
  color = "black",
  hoverColor = "white",
  isHover = false,
}) => {
  return (
    <svg
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        style={{ transition: "all 0.1s linear" }}
        d="M6 15L12 9L18 15"
        stroke={isHover ? hoverColor : color}
        strokeWidth="1.5"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
};

export default ChevronUpIcon;
