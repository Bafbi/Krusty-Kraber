const {withMaterialColors} = require('tailwind-material-colors');

/** @type {import('@types/tailwindcss/tailwind-config')} */
module.exports = withMaterialColors({
    content: [
        '../src/main/java/**/*.java'
    ],
    theme: {
        extend: {},
    },
    variants: {
        extend: {},
    },
    plugins: [
        require('@tailwindcss/typography'),
    ],
}, {primary: '#D0BCFF',success:'#3ee81d'});