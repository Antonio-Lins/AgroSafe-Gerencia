
import js from "@eslint/js";
import globals from "globals";
import react from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";

const arquivos = ["src/**/*.{js,jsx}"];

export default [
    {
        ignores: ["dist/**", "node_modules/**"],
    },
    {
        ...js.configs.recommended,
        files: arquivos,
    },
    {
        ...react.configs.flat.recommended,
        files: arquivos,
        settings: {
            react: {
                version: "detect",
            },
        },
        rules: {
            ...react.configs.flat.recommended.rules,
            "react/prop-types": "off",
        },
    },
    {
        ...react.configs.flat["jsx-runtime"],
        files: arquivos,
    },
    {
        files: arquivos,
        plugins: {
            "react-hooks": reactHooks,
        },
        rules: {
            "react-hooks/rules-of-hooks": "error",
            "react-hooks/exhaustive-deps": "warn",
        },
    },
    {
        files: arquivos,
        languageOptions: {
            ecmaVersion: "latest",
            sourceType: "module",
            globals: globals.browser,
            parserOptions: {
                ecmaFeatures: {
                    jsx: true,
                },
            },
        },
    },
];
