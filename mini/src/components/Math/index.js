import Big from 'big.js';

export default {
  install: function (Vue) {
    Object.defineProperty(Vue.prototype, '$bigjs', {
      get() {
        return (v) => {
          if (v == "" || isNaN(v) || !v)
            v = 0;
          return new Big(v);
        }
      }
    })
  }
}

