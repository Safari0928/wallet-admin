<!-- 个人资料 -->
<template>
    <div class="user-setting">
        <el-card class="!border-none" shadow="never">
            <el-form ref="formRef" class="ls-form" :model="formData" :rules="rules" label-width="160px">
                <div class="justify-center flex flex-col items-center">

                    <el-form-item label="username：" prop="username">
                        <div class="w-80">
                            <el-input v-model="formData.username" disabled />
                        </div>
                    </el-form-item>

                    <el-form-item label="nickname: " prop="nickname">
                        <div class="w-80">
                            <el-input v-model="formData.nickname" placeholder="Please enter name" />
                        </div>
                    </el-form-item>

                    <el-form-item label="current Password：" prop="oldPassword">
                        <div class="w-80">
                            <el-input v-model.trim="formData.oldPassword" placeholder="current Password" type="password"
                                show-password />
                        </div>
                    </el-form-item>

                    <el-form-item label="password:" prop="newPassword">
                        <div class="w-80">
                            <el-input v-model.trim="formData.newPassword" placeholder="password" type="password"
                                show-newPassword />
                        </div>
                    </el-form-item>

                    <el-form-item label="passwordConfirm:" prop="passwordConfirm">
                        <div class="w-80">
                            <el-input v-model.trim="formData.passwordConfirm" placeholder="password Confirm" type="password"
                                show-password />
                        </div>
                    </el-form-item>

                    <el-button class="w-1/3 " type="primary" @click="handleSubmit">Save</el-button>

                </div>
            </el-form>
        </el-card>
        <footer-btns>
        </footer-btns>
    </div>
</template>

<script setup lang="ts" name="userSetting">
import { setUserInfo } from '@/api/user'
import useUserStore from '@/stores/modules/user'
import feedback from '@/utils/feedback'
import type { FormInstance } from 'element-plus'
const formRef = ref<FormInstance>()
const userStore = useUserStore()

const formData = reactive({
    username: '',
    nickname: '',
    oldPassword: '',
    newPassword: '',
    passwordConfirm: ''
})


const rules = reactive<object>({

    nickname: [
        {
            required: true,
            message: '请输入名称',
            trigger: ['blur']
        }
    ],
    oldPassword: [
        {
            validator: (rule: object, value: string, callback: any) => {
                if (formData.newPassword) {
                    if (!value) callback(new Error('Please enter current password'))
                }
                callback()
            },
            trigger: 'blur'
        }
    ],
    newPassword: [
        {
            validator: (rule: object, value: string, callback: any) => {
                if (formData.oldPassword) {
                    if (!value) callback(new Error('Please enter new password'))
                }
                callback()
            },
            trigger: 'blur'
        }
    ],
    passwordConfirm: [
        {
            validator: (rule: object, value: string, callback: any) => {
                if (formData.newPassword) {
                    if (!value) callback(new Error('Please enter password again'))
                    if (value !== formData.newPassword) callback(new Error('The password entered twice is inconsistent!'))
                }
                callback()
            },
            trigger: 'blur'
        }
    ]
})

const getUser = async () => {
    const userInfo = userStore.userInfo
    for (const key in formData) {

        formData[key] = userInfo[key]
    }
}


const setUser = async () => {
    await setUserInfo(formData)
    feedback.msgSuccess('saved correctly')
    userStore.getUserInfo()
    userStore.logout()
}


const handleSubmit = async () => {
    await formRef.value?.validate()
    setUser()
}

getUser()
</script>

<style lang="scss" scoped></style>
