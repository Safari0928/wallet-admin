<template>
    <div class="user-detail">
        <el-card class=" mx-auto p-8 bg-white rounded-lg shadow-md">

            <div class="grid grid-cols-2 gap-6">

                <div class="col-span-2 md:col-span-1 flex-col justify-between items-center">
                    <div class="font-medium text-xl m-2">
                        User Id: {{ userData.payId }}
                    </div>
                    <div class="grid grid-cols-3">
                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Phone Number:</strong></div>
                            <strong> {{ userData.phoneNumber }}</strong>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Country:</strong></div>
                            <strong>{{ userData.country }}</strong>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Email:</strong></div>
                            <strong>{{ userData.email }}</strong>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Verify:</strong></div>
                            <span
                                :style="userData.verifyAccount === 1 ? 'color: green' : userData.verifyAccount === 2 ? 'color: orange' : 'color:red'"><strong>{{
                                    userData.verifyAccountMSG }}</strong></span>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Status:</strong></div>
                            <span :style="userData.status === 1 ? 'color: green' : 'color: red'"><strong>{{
                                userData.statusMSG }}</strong></span>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Nick name:</strong></div>
                            <strong>{{ userData.nickName }}</strong>
                        </div>

                        <div class="m-2 p-3 rounded drop-shadow-md flex-col justify-center items-center text-sm">
                            <div class="font-light opacity-50 mb-3"><strong>Creation Time:</strong></div>
                            <strong>{{ userData.createTime }}</strong>
                        </div>

                    </div>
                </div>


                <div class=" col-span-2 md:col-span-1 m-2">

                    <div class="flex justify-end">
                        <div v-if="userData.status == -1">

                            <el-button @click="() => updateUser({ type: 0 })">Enable</el-button>
                        </div>
                        <div v-else>
                            <el-button @click="() => updateUser({ type: 0 })">Disable</el-button>
                            <span class="m-2" v-if="userData.verifyAccount == 2">
                                <el-button type="primary"
                                    @click="() => updateUser({ type: 1, status: 1 })">Verify</el-button>
                            </span>

                        </div>
                    </div>
                    <h2 class="text-lg font-semibold">Balances</h2>
                    <div class="flex flex-col justify-between h-full">
                        <div class="grid grid-cols-3 gap-3">
                            <div
                                class="border rounded border-opacity-75 drop-shadow-md w-full h-full m-3 flex flex-col justify-center  ">
                                <div class="ml-6">

                                    <div class="text-sm opacity-60 ">TRY:</div>
                                    <div class=" text-3xl font-semibold">

                                        {{ userData.balanceForTry }} </div>
                                </div>
                            </div>
                            <div
                                class="border rounded border-opacity-75 drop-shadow-md w-full h-full m-3 flex flex-col justify-center ">
                                <div class="ml-6">

                                    <div class="text-sm opacity-60 ">USD:</div>
                                    <div class=" text-3xl font-semibold">

                                        {{ userData.balanceForUsd }} </div>
                                </div>
                            </div>
                            <div
                                class="border rounded border-opacity-75 drop-shadow-md w-full h-full m-3 flex flex-col justify-center ">
                                <div class="ml-6">

                                    <div class="text-sm opacity-60 ">EUR:</div>
                                    <div class=" text-3xl font-semibold">

                                        {{ userData.balanceForEur }}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="flex flex-col justify-end mb-16">
                        <div class="text-sm opacity-60">KYC</div>
                        <div class="font-extralight text-xl" style="color: red;">NONE</div>
                    </div>
                </div>
            </div>
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <el-tabs v-model="activeTab">
                <el-tab-pane label="Transactions" name="transactions">
                    <el-form-item class="flex justify-self-end">
                        <el-button type="primary" @click="6">Export Selected</el-button>
                    </el-form-item>
                    <el-table v-loading="transaction.pager.loading" :data="transaction.pager.lists">
                        <el-table-column type="selection" width="55" align="center"></el-table-column>

                        <el-table-column label="Transaction Number" prop="transactionNumber" min-width="160"
                            show-overflow-tooltip>
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    <el-tooltip class="box-item" effect="dark" :content="row.transactionNumber"
                                        placement="top">
                                        {{ formatTransactionNumber(row.transactionNumber) }}
                                    </el-tooltip>
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="Currency" prop="currency" min-width="100" />
                        <el-table-column label="Amount (TL)" prop="amount" min-width="140" />
                        <el-table-column label="Real Amount (TL)" prop="realAmount" min-width="160" />
                        <el-table-column label="User Id" prop="payId" min-width="160" />

                        <el-table-column label="status" min-width="160">
                            <template #header>
                                <el-form :model="queryParams" :inline="true" class="mt-4">
                                    <el-form-item>
                                        <el-select v-model="statusFilter" placeholder="Status" @change="handleFilterChange">
                                            <el-option label="All" value=""></el-option>
                                            <el-option label="Success" value="1" />
                                            <el-option label="Failed" value="-1" />
                                            <el-option label="Pending" value="2" />
                                        </el-select>
                                    </el-form-item>
                                </el-form>
                            </template>
                            <template #default="{ row }">
                                <span
                                    :style="row.status === 1 ? 'color: green' : row.status === 2 ? 'color: orange' : 'color: red'"
                                    style="border: 2px; padding: 2px;">
                                    {{ row.statusMSG }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="typeDetail" min-width="160">
                            <template #header>
                                <el-form :model="queryParams" :inline="true" class="mt-4">
                                    <el-form-item>
                                        <el-select v-model="typeFilter" placeholder="Type" @change="handleFilterChange">
                                            <el-option label="All" value=""></el-option>
                                            <el-option label="Deposit" value="1"></el-option>
                                            <el-option label="Withdrawal" value="2"></el-option>
                                            <el-option label="Transfer Deposit" value="3"></el-option>
                                            <el-option label="Transfer Withdrawal" value="4"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-form>
                            </template>
                            <template #default="{ row }">
                                {{ row.typeMSG }}
                            </template>
                        </el-table-column>
                        <el-table-column label="Creation Time" min-width="200">
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTimeIfCurrentYear(row.createTime) }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="Complete Time" min-width="200">
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTimeIfCurrentYear(row.completeTime) }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="Action" width="120" fixed="right">
                            <template #default="{ row }">
                                <el-button type="primary" link @click="handleDetail(row.detailId, row.typeDetail)">
                                    detail
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="transaction.pager" @change="transaction.getLists" />
                    </div>
                </el-tab-pane>
                <el-tab-pane label="Cards" name="cards">
                    <el-form-item class="flex justify-self-end">
                        <el-button type="primary" @click="6">Export Selected</el-button>
                    </el-form-item>
                    <el-table v-loading="bankCard.pager.loading" :data="bankCard.pager.lists">
                        <el-table-column type="selection" width="55" align="center"></el-table-column>

                        <el-table-column label="Card Number" prop="cardNumber" min-width="160" show-overflow-tooltip>
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTransactionNumber(row.cardNumber) }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="Expiry Date" prop="expiryDate" min-width="100" />
                        <el-table-column label="CVV/CVC/CVN" prop="cvv" min-width="140" />
                        <el-table-column label="Full Name" prop="fullName" min-width="160" />
                        <el-table-column label="Bank Name" prop="bankName" min-width="160" />
                        <el-table-column label="Card Type" prop="cardType" min-width="120" />
                        <el-table-column label="Creation Time" min-width="200">
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTimeIfCurrentYear(row.createTime) }}
                                </span>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="bankCard.pager" @change="bankCard.getLists" />
                    </div>
                </el-tab-pane>
                <el-tab-pane label="Accounts" name="Accounts">
                    <el-form-item class="flex justify-self-end">
                        <el-button type="primary" @click="6">Export Selected</el-button>
                    </el-form-item>
                    <el-table v-loading="bankAccount.pager.loading" :data="bankAccount.pager.lists">
                        <el-table-column type="selection" width="55" align="center"></el-table-column>

                        <el-table-column label="Iban" prop="iban" min-width="160" show-overflow-tooltip>
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTransactionNumber(row.iban) }}
                                </span>
                            </template>
                        </el-table-column>
                        <el-table-column label="Country" prop="country" min-width="100" />
                        <el-table-column label="Bank Card Name" prop="bankCardName" min-width="140" />
                        <el-table-column label="Bank Name" prop="bankName" min-width="160" />
                        <el-table-column label="Card Type" prop="cardType" min-width="120" />
                        <el-table-column label="Creation Time" min-width="200">
                            <template #default="{ row }">
                                <span style="border: 2px; padding: 2px;">
                                    {{ formatTimeIfCurrentYear(row.createTime) }}
                                </span>
                            </template>
                        </el-table-column>

                    </el-table>
                    <div class="flex justify-end mt-4">
                        <pagination v-model="bankAccount.pager" @change="bankAccount.getLists" />
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-card>
        <deposit-popup v-if="showDetail" ref="depositPopupRef" @success="transaction.getLists()"
            @close="showDetail = false" />
        <withdrawal-popup v-if="showDetail" ref="withdrawalPopupRef" @success="transaction.getLists()"
            @close="showDetail = false" />
        <transfer-popup v-if="showDetail" ref="transferPopupRef" @success="transaction.getLists()"
            @close="showDetail = false" />
    </div>
</template>
  
<script lang="ts" setup name="userDetail">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { userDetail, setUser } from '@/api/user'
import { transactionList } from '@/api/transaction'
import { getBankAccount } from '@/api/bankaccount'
import { getBankCard } from '@/api/bankcard'
import { usePaging } from '@/hooks/usePaging'
import { formatTransactionNumber, formatTimeIfCurrentYear } from '@/utils/filters'
import feedback from '@/utils/feedback'
import depositPopup from '@/views/deposit/detail.vue'
import transferPopup from '@/views/transfer/detail.vue'
import withdrawalPopup from '@/views/withdrawal/detail.vue'


const depositPopupRef = shallowRef<InstanceType<typeof depositPopup>>();
const withdrawalPopupRef = shallowRef<InstanceType<typeof withdrawalPopup>>();
const transferPopupRef = shallowRef<InstanceType<typeof transferPopup>>();

const route = useRoute()
const userData = ref({
    avatar: '',
    uuid: '',
    phoneNumber: '',
    verifyAccount: -1,
    verifyAccountMSG: '',
    status: 1,
    statusMSG: '',
    nickName: '',
    email: '',
    currencyTRY: 'TRY',
    currencyUSD: 'USD',
    currencyEUR: 'EUR',
    balanceForTry: 0,
    balanceForUsd: 0,
    balanceForEur: 0,
    createTime: '',
})

const showDetail = ref(false)
const statusFilter = ref('');
const typeFilter = ref('');
const activeTab = ref('transactions'); // Default to the User Details tab


const updateUser = async (data: any) => {
    await feedback.confirm("are you sure?")
    await setUser({
        ...data,
        userId: route.query.id
    })
    getDetails()
}

const getDetails = async () => {
    try {
        const data = await userDetail({ uuid: route.query.id })
        userData.value = data

    } catch (error) {
        console.error('Error fetching user details:', error)
    }
}

const handleFilterChange = () => {
    queryParams.status = statusFilter.value;
    queryParams.typeDetail = typeFilter.value;
    transaction.resetPage();
};


const queryParams = reactive({
    userId: route.query.id,
    status: '',
    typeDetail: '',
})

const handleDetail = async (detailId: any, typeDetail: any) => {
    showDetail.value = true;
    await nextTick();

    if (typeDetail === 1) {
        // Deposit
        depositPopupRef.value?.open();
        depositPopupRef.value?.getDetail(detailId);
    } else if (typeDetail === 2) {
        // Transfer
        withdrawalPopupRef.value?.open();
        withdrawalPopupRef.value?.getDetail(detailId);
    }
    else if (typeDetail === 3 || typeDetail == 4) {
        // Transfer
        transferPopupRef.value?.open();
        transferPopupRef.value?.getDetail(detailId);
    }
}


const transaction = usePaging({
    fetchFun: transactionList,
    params: queryParams
})

const bankCard = usePaging({
    fetchFun: getBankCard,
    params: queryParams
})

const bankAccount = usePaging({
    fetchFun: getBankAccount,
    params: queryParams
})


onMounted(() => {
    if (route.query.id) {
        getDetails()
        transaction.getLists()
        bankCard.getLists()
        bankAccount.getLists()
    }

})

transaction.getLists()
</script>
  