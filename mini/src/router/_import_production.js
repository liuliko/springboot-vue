// src/router/_import_production.js
module.exports = file => () => require(`@/views/${file}.vue`)
