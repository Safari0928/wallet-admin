<template>
    <div>

        <el-card class="!border-none" shadow="never">
            <div class="p-3">

                <div class="flex justify-between p-3" v-if="!isEditingPayCommission">
                    <span>
                        <div class="font-bold">Aipay Handling rate (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee: <strong>{{ data.payCommission * 100 }}%</strong>
                        </div>
                    </span>
                    <el-button @click="startEditing('payCommission')">Edit</el-button>
                </div>
                <div class="flex justify-between p-3" v-if="isEditingPayCommission">

                    <span>
                        <div class="font-bold">Aipay Handling rate (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee: <el-input v-model="data.payCommission"></el-input>
                        </div>
                    </span>
                    <div>
                        <el-button @click="showPassword">Submit</el-button>
                        <el-button @click="cancelEdit('payCommission')">Cancel</el-button>
                    </div>
                </div>
                <hr class="mt-2 opacity-40">

                <div class="flex justify-between p-3" v-if="!isEditingUnverifiedUserFee">
                    <span>
                        <div class="font-bold">Withdrawal limit for registered users (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee: <strong>{{ data.unverifiedUserFee }}</strong>
                        </div>
                    </span>
                    <el-button @click="startEditing('unverifiedUserFee')">Edit</el-button>
                </div>
                <div class="flex justify-between p-3" v-if="isEditingUnverifiedUserFee">
                    <span>
                        <div class="font-bold">Withdrawal limit for registered users (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee:
                            <el-input v-model="data.unverifiedUserFee"></el-input>
                        </div>
                    </span>
                    <div>
                        <el-button @click="showPassword">Submit</el-button>
                        <el-button @click="cancelEdit('unverifiedUserFee')">Cancel</el-button>
                    </div>
                </div>
                <hr class="mt-2 opacity-40">
                <div class="flex justify-between p-3" v-if="!isEditingVerifiedUserFee">
                    <span>
                        <div class="font-bold">Withdrawal limit for verified users (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee: <strong>{{ data.verifiedUserFee }}</strong>
                        </div>
                    </span>
                    <el-button @click="startEditing('verifiedUserFee')">Edit</el-button>
                </div>
                <div class="flex justify-between p-3" v-if="isEditingVerifiedUserFee">
                    <span>
                        <div class="font-bold">Withdrawal limit for verified users (TRY)</div>
                        <div class="text-xs font-medium opacity-70">
                            Platform's current handling fee:
                            <el-input v-model="data.verifiedUserFee"></el-input>
                        </div>
                    </span>
                    <div>
                        <el-button @click="showPassword">Submit</el-button>
                        <el-button @click="cancelEdit('verifiedUserFee')">Cancel</el-button>
                    </div>
                </div>
                <hr class="mt-2 opacity-40">
                <div class="flex justify-between p-3" v-if="!isEditingAmountAudiet">
                    <span>
                        <div class="font-bold">Amount audited（TRY）</div>
                        <div class="text-xs font-medium opacity-70">

                            Each withdrawal greater than this amount is subject to audit： <strong>{{ data.amountAudited
                            }}</strong>
                        </div>
                    </span>
                    <el-button @click="startEditing('amountAudiet')">Edit</el-button>
                </div>
                <div class="flex justify-between p-3" v-if="isEditingAmountAudiet">
                    <span>
                        <div class="font-bold">Amount audited（TRY）</div>
                        <div class="text-xs font-medium opacity-70">
                            Each withdrawal greater than this amount is subject to audit：
                            <el-input v-model="data.amountAudited"></el-input>
                        </div>
                    </span>
                    <div>
                        <el-button @click="showPassword">Submit</el-button>
                        <el-button @click="cancelEdit('amountAudiet')">Cancel</el-button>
                    </div>
                </div>
                <hr class="mt-2 opacity-40">
            </div>
        </el-card>
        <div :class="{ 'opacity-100': isPasswordShow, 'opacity-0': !isPasswordShow }"
            class="transition-opacity duration-500">
            <div class="text-2xl font-semibold flex justify-center items-center m-3">
                Please enter the password
            </div>
            <el-card class="mt-4">
                <el-input type="password" v-model="password"></el-input>
                <el-button type="primary" class="w-full my-4" @click="submitEdit"> accept the changes</el-button>
            </el-card>
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { ref, onActivated } from 'vue';
import { configureWithdraw, configureWithdrawUpdate } from '@/api/configure';

const password = ref('');

const showPassword = () => {
    isPasswordShow.value = !isPasswordShow.value;
};

const data = ref({
    amountAudited: 0,
    payCommission: 0,
    unverifiedUserFee: 0,
    verifiedUserFee: 0
});

const isEditingPayCommission = ref(false);
const isEditingUnverifiedUserFee = ref(false);
const isEditingVerifiedUserFee = ref(false);
const isEditingAmountAudiet = ref(false);
const isPasswordShow = ref(false);



const startEditing = (field) => {
    if (field === 'payCommission') {
        isEditingPayCommission.value = true;
        newPayCommission.value = data.value.payCommission;
    } else if (field === 'unverifiedUserFee') {
        isEditingUnverifiedUserFee.value = true;
        newUnverifiedUserFee.value = data.value.unverifiedUserFee;
    } else if (field === 'verifiedUserFee') {
        isEditingVerifiedUserFee.value = true;
        newVerifiedUserFee.value = data.value.verifiedUserFee;
    }
    else if (field === 'amountAudiet') {
        isEditingAmountAudiet.value = true;
        newAmountAudiet.value = data.value.amountAudiet;
    }
};

const submitEdit = async () => {
    try {

        await configureWithdrawUpdate({
            ...data.value,
            uuid: data.value.uuid,

            password: password.value

        });

        isEditingUnverifiedUserFee.value = false;

        isEditingVerifiedUserFee.value = false;

        isEditingAmountAudiet.value = false;

        isPasswordShow.value = false
        fetchData()
    } catch (error) {
        console.error('Error updating data:', error);
    }
};

const cancelEdit = (field) => {
    if (field === 'payCommission') {
        isEditingPayCommission.value = false;
    } else if (field === 'unverifiedUserFee') {
        isEditingUnverifiedUserFee.value = false;
    } else if (field === 'verifiedUserFee') {
        isEditingVerifiedUserFee.value = false;
    } else if (field === 'amountAudiet') {
        isEditingAmountAudiet.value = false;
    }
};

const fetchData = async () => {
    try {
        const result = await configureWithdraw();
        data.value = result;
        console.log(result);

    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

onActivated(() => {
    fetchData();
});

fetchData();
</script>
  