<template>
  <div>
    <el-input type="text" :value="_moneyValue" @input="inputChange" :disabled="disabled"></el-input>
  </div>
</template>
<script>
  import {createNotification} from '@/api/notification'


  //金额输入框
  export default {
    name: "el-amountinput",
    computed: {
      _moneyValue() {
        let moneyValue = ""
        if (this.value === '')
          moneyValue = ''
        else if (isNaN(this.value))
          moneyValue = ''
        else
          moneyValue =(this.value||this.value===0?this.$bigjs(this.value).div(100):"") + (this.point ? "." : "")+ (this.pointZero ? ".0" : "")
        return moneyValue
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    },
    props: {
      value: {
        defalut: undefined
      },
      disabled: {
        type: Boolean,
        default: false
      },
      maxValue: {
        type: Number,
        default: 999999999
      }
    },
    data() {
      return {
        point: false,
        pointZero:false
      }
    },
    methods: {
      inputChange(v) {
        this.point = v&&v.endsWith("\.") && !isNaN(v)
        this.pointZero=v&&v.endsWith("\.0") && !isNaN(v)

        if (isNaN(v)||v=="") {
          this.$emit('change', undefined)
        } else {
          let floatV = Number.parseFloat(v)
          let coinValue = this.$bigjs(floatV).times(100).toFixed(0)
          if (coinValue > this.maxValue) {
            createNotification("超出最大金额限制", "error")
            this.$emit('change', this.maxValue)
          } else{
            this.$emit('change', coinValue)
          }
        }
      }
    }, mounted() {

    }
  }
</script>
