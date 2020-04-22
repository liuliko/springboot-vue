<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>

    <breadcrumb class="breadcrumb-container"/>

    <div class="right-menu" style="padding-right: 35px">

      <el-dropdown>
        <el-button type="primary" size="small">
          {{name}}<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <div @click="editpassowrd">修改密码</div>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <div @click="logout">登出</div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog v-el-drag-dialog :close-on-click-modal="false" width="40%" title="密码修改" :visible.sync="dialogFormVisible" :appendToBody="true" @close="resetFormField">
      <el-form :model="resetPasswordForm" status-icon :rules="rules" ref="apiform" label-width="100px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input type="password" v-model="resetPasswordForm.oldPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="resetPasswordForm.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="repeatPassword">
          <el-input type="password" v-model="resetPasswordForm.repeatPassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="formcommit">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
  import {mapGetters} from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'
  import {createLoading} from '@/api/loading'
  import {createNotification} from '@/api/notification'
  import request from '@/utils/request'
  import {billTitle} from '@/api/user'

  export default {
    components: {
      Breadcrumb,
      Hamburger
    },
    data() {

      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.resetPasswordForm.newPassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };

      return {
        dialogFormVisible: false,
        resetPasswordForm: {
          oldPassword: "",
          newPassword: "",
          repeatPassword: ""
        },
        rules: {
          oldPassword: [
            {required: true, message: '请输入旧密码', trigger: 'blur'}]
          ,
          newPassword: [
            {required: true, message: '请输入新密码', trigger: 'blur'},
            {
              validator: function (rule, value, callback) {
                var pattern = /^.*(?=.{6,16})(?=.*\d)(?=.*[A-Za-z]{1,}).*$/;
                var passed=pattern.test(value)&&value.length>=6&&value.length<=16
                if(passed){
                  callback()
                }else{
                  callback(new Error("密码必须是6~16位,并且必须同时包含数组和字母"))
                }
              }
            },
            {max: 16, message: '请勿输入超过16个字符', trigger: 'blur'}],
          repeatPassword: [
            {validator: validatePass2, trigger: 'blur'}],
        },

      }
    },
    computed: {
      ...mapGetters([
        'sidebar',
        'avatar',
        'name'
      ])
    },
    methods: {

      toggleSideBar() {
        this.$store.dispatch('app/toggleSideBar')
      },
      async logout() {
        await this.$store.dispatch('user/logout')
        this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      },
      formcommit() {
        let vApiform = this.$refs.apiform
        vApiform.validate(valid => {
          if (valid) {
            let url = '/sys/SysUser/updatePassword.html'
            let loading = createLoading()
            request({
              url: url,
              method: 'post',
              params: this.resetPasswordForm
            }).then(data => {
              loading.close()
              if (data.data.flag == "ok") {
                createNotification('保存成功', 'success')
                this.dialogFormVisible = false
              } else {
                console.log(data)
                createNotification(data.data.message, 'error')
              }
            }).catch(() => {
              loading.close()
            })

          } else {
            return false
          }
        })
      },
      editpassowrd: function () {
        this.dialogFormVisible = true
      },
      resetFormField() {
        this.$refs.apiform.resetFields()
      }
    },
    mounted() {

    }
  }
</script>

<style lang="scss" scoped>
  .navbar {
    height: 50px;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

    .hamburger-container {
      line-height: 46px;
      height: 100%;
      float: left;
      cursor: pointer;
      transition: background .3s;
      -webkit-tap-highlight-color: transparent;

      &:hover {
        background: rgba(0, 0, 0, .025)
      }
    }

    .breadcrumb-container {
      float: left;
    }

    .right-menu {
      float: right;
      height: 100%;
      line-height: 50px;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background .3s;

          &:hover {
            background: rgba(0, 0, 0, .025)
          }
        }
      }

      .avatar-container {
        margin-right: 30px;

        .avatar-wrapper {
          margin-top: 5px;
          position: relative;

          .user-avatar {
            cursor: pointer;
            width: 40px;
            height: 40px;
            border-radius: 10px;
          }

          .el-icon-caret-bottom {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 25px;
            font-size: 12px;
          }
        }
      }
    }
  }
</style>
