package com.brezze.kotlin92.ui.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.brezze.kotlin92.BR
import com.brezze.kotlin92.R
import com.brezze.kotlin92.databinding.ActivityPosBinding
import com.brezze.library_common.base.BaseActivity
import com.brezze.library_common.utils.LogUtils
import com.dspread.xpos.QPOSService
import com.dspread.xpos.QPOSService.DoTradeResult
import com.dspread.xpos.QPOSService.TransactionResult
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_pos.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

open class PosActivity : BaseActivity<ActivityPosBinding,PosViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_pos

    override fun initVariableId(): Int = BR.viewModel

    private var pos: QPOSService? = null

    /**
     * 【位置管理】
     */
    private var lm: LocationManager? = null

    private enum class POS_TYPE {
        BLUETOOTH, AUDIO, UART, USB, OTG, BLUETOOTH_BLE
    }

    private val posType = POS_TYPE.BLUETOOTH


    override fun initData() {
        super.initData()
        initPermission()

        var deviceList = pos?.deviceList

        btnBT.setOnClickListener {
            openPos()
            pos?.clearBluetoothBuffer()
            pos?.scanQPos2Mode(this,20)
        }

        doTradeButton.setOnClickListener {
            doTrade()
        }

        disconnect.setOnClickListener {
            close()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun doTrade() {
        statusEditText.setText("starting ....")
        pos?.doTrade(0,30)
    }

    private fun openPos() {
        pos = QPOSService.getInstance(QPOSService.CommunicationMode.BLUETOOTH)
        val  handler = Handler(Looper.myLooper()!!)
        pos?.setConext(this)
        val myPosListener = MyPosListener()
        pos?.initListener(handler, myPosListener)
    }

    private fun close(){
        pos?.disconnectBT()
    }


    @SuppressLint("CheckResult")
    private fun initPermission() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter != null && !adapter.isEnabled) { //表示蓝牙不可用 add one fix
            val enabler = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enabler)
        }
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val ok = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (ok!!){
            RxPermissions(this).requestEach(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                if (it.granted){

                }else{
                    toast("没权限")
                }

            }
        }else{
            Log.e("BRG", "系统检测到未开启GPS定位服务")
            toast("系统检测到未开启GPS定位服务")
            val intent = Intent()
            intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            startActivityForResult(intent, 1315)
        }

    }

    open fun selectBTPos(blueTootchAddress:String){
        doTradeButton.isEnabled = true
        disconnect.isEnabled = true
        pos?.stopScanQPos2Mode()
        pos?.connectBluetoothDevice(true,25,blueTootchAddress)
    }

    override fun onPause() {
        super.onPause()
        pos?.stopScanQPos2Mode()
    }

    inner class MyPosListener : QPOSService.QPOSServiceListener{
        /**
         * 返回选择的开始，返回交易的结果
         */
        override fun onDoTradeResult(
            result: QPOSService.DoTradeResult?,
            decodeData: Hashtable<String, String>?
        ) {
            LogUtils.d("(DoTradeResult result, Hashtable<String, String> decodeData) ","${result.toString()} >>>decodeData: $decodeData")
            when(result){
                DoTradeResult.NONE->{}
                DoTradeResult.ICC->{
                    pos?.doEmvApp(QPOSService.EmvOption.START)
                }
                DoTradeResult.NOT_ICC->{}
                DoTradeResult.BAD_SWIPE->{}
                DoTradeResult.MCR->{}
                else->{}

            }
        }

        override fun onReturnPowerOnNFCResult(p0: Boolean, p1: String?, p2: String?, p3: Int) {

        }

        override fun onRequestQposConnected() {

        }

        override fun onReturnReversalData(p0: String?) {

        }

        override fun onReturnSetMasterKeyResult(p0: Boolean) {

        }

        override fun onRequestOnlineProcess(tlv: String?) {
            LogUtils.d("onRequestOnlineProcess")
            LogUtils.d(tlv)
            val decodeData = pos?.anlysEmvIccData(tlv)
            LogUtils.d("anlysEmvIccData(tlv):$decodeData")
            val str = "8A023030" //Currently the default value,
            pos?.sendOnlineProcessResult(str) //脚本通知/55域/ICCDATA

        }

        override fun onReturnPowerOnIccResult(p0: Boolean, p1: String?, p2: String?, p3: Int) {

        }

        override fun onSetBuzzerResult(p0: Boolean) {

        }

        override fun onBatchWriteMifareCardResult(
            p0: String?,
            p1: Hashtable<String, MutableList<String>>?
        ) {

        }

        override fun onRequestSelectEmvApp(p0: ArrayList<String>?) {

        }

        override fun onQposDoTradeLog(p0: Boolean) {

        }

        override fun onVerifyMifareCardResult(p0: Boolean) {

        }

        override fun onGetSleepModeTime(p0: String?) {

        }

        override fun onSetBuzzerStatusResult(p0: Boolean) {

        }

        override fun onRequestTransactionResult(transactionResult: QPOSService.TransactionResult?) {
            LogUtils.d("onRequestTransactionResult")
            when(transactionResult){
                TransactionResult.APPROVED->{
                    toast("success")
                }
                TransactionResult.CANCEL->{}
                TransactionResult.CARD_REMOVED->{}
                TransactionResult.TERMINATED->{}
                else->{}

            }
        }

        override fun onRequestQposDisconnected() {

        }

        override fun onRequestTransactionLog(p0: String?) {

        }

        override fun onReturnGetQuickEmvResult(p0: Boolean) {

        }

        override fun onSetSleepModeTime(p0: Boolean) {

        }

        override fun onEncryptData(p0: String?) {

        }

        override fun onSetManagementKey(p0: Boolean) {

        }

        override fun onLcdShowCustomDisplay(p0: Boolean) {

        }

        override fun transferMifareData(p0: String?) {

        }

        override fun onUpdatePosFirmwareResult(p0: QPOSService.UpdateInformationResult?) {

        }

        override fun onReadBusinessCardResult(p0: Boolean, p1: String?) {

        }

        override fun onReturnPowerOffNFCResult(p0: Boolean) {

        }

        override fun onReturnSignature(p0: Boolean, p1: String?) {

        }

        override fun onRequestCalculateMac(p0: String?) {

        }

        override fun onReturnConverEncryptedBlockFormat(p0: String?) {

        }

        override fun onDeviceFound(p0: BluetoothDevice?) {
            if (p0 != null){
                p0.address?.let {
                    Log.d("onDeviceFound","$it >>>> ${p0.name}")
                    if (p0.name.contains("MPOS")){
                        selectBTPos(it)
                    }

                }

            }
        }


        override fun onRequestFinalConfirm() {

        }

        override fun onBluetoothBondTimeout() {

        }

        override fun onRequestTime() {
            val terminalTime = SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault())
                .format(Calendar.getInstance().time)
            pos?.sendTime(terminalTime)
            statusEditText.setText("Request Terminal Time. Replied $terminalTime")
        }

        override fun onRequestNoQposDetectedUnbond() {

        }

        override fun onRequestWaitingUser() {

        }

        override fun onConfirmAmountResult(p0: Boolean) {

        }

        override fun onSetPosBlePinCode(p0: Boolean) {

        }

        override fun onReturniccCashBack(p0: Hashtable<String, String>?) {

        }

        override fun writeMifareULData(p0: String?) {

        }

        override fun verifyMifareULData(p0: Hashtable<String, String>?) {

        }

        override fun onGetKeyCheckValue(p0: MutableList<String>?) {

        }

        override fun onRequestBatchData(p0: String?) {
            LogUtils.d("ICC交易结束")
            LogUtils.d("onRequestBatchData(String tlv):$p0")
        }

        override fun onQposInfoResult(p0: Hashtable<String, String>?) {

        }

        override fun onReturnGetEMVListResult(p0: String?) {

        }

        override fun getMifareReadData(p0: Hashtable<String, String>?) {

        }

        override fun onReturnGetPinResult(p0: Hashtable<String, String>?) {

        }

        override fun onQposIsCardExist(p0: Boolean) {

        }

        override fun onUpdateMasterKeyResult(p0: Boolean, p1: Hashtable<String, String>?) {

        }

        override fun onCbcMacResult(p0: String?) {

        }

        override fun onGetPosComm(p0: Int, p1: String?, p2: String?) {

        }

        override fun onFinishMifareCardResult(p0: Boolean) {

        }

        override fun onBluetoothBonded() {

        }

        override fun onRequestDisplay(displayMsg: QPOSService.Display?) {
            when(displayMsg){
                QPOSService.Display.CARD_REMOVED->{

                }
                QPOSService.Display.CLEAR_DISPLAY_MSG->{}
                QPOSService.Display.MSR_DATA_READY->{}
                else->{}
            }
        }

        override fun onReadMifareCardResult(p0: Hashtable<String, String>?) {

        }

        override fun onError(p0: QPOSService.Error?) {

        }

        override fun onReturnPowerOffIccResult(p0: Boolean) {

        }

        override fun onBluetoothBondFailed() {

        }

        override fun onQposIsCardExistInOnlineProcess(p0: Boolean) {

        }

        /**
         * 判断是否请求在线连接请求
         * TODO 简单描述该方法的实现功能（可选）
         **/
        override fun onRequestIsServerConnected() {
            LogUtils.d("onRequestIsServerConnected")
            pos?.isServerConnected(true)
        }

        override fun onRequestNoQposDetected() {

        }

        override fun onBluetoothBoardStateResult(p0: Boolean) {

        }

        override fun onWriteMifareCardResult(p0: Boolean) {

        }

        override fun onReturnApduResult(p0: Boolean, p1: String?, p2: Int) {

        }

        override fun onReturnUpdateEMVRIDResult(p0: Boolean) {

        }

        override fun onQposKsnResult(p0: Hashtable<String, String>?) {

        }

        override fun onGetDevicePubKey(p0: String?) {

        }

        override fun onReturnSetAESResult(p0: Boolean, p1: String?) {

        }

        override fun onReturnNFCApduResult(p0: Boolean, p1: String?, p2: Int) {

        }

        override fun onWriteBusinessCardResult(p0: Boolean) {

        }

        override fun onReturnSetSleepTimeResult(p0: Boolean) {

        }

        override fun onSetBuzzerTimeResult(p0: Boolean) {

        }

        override fun onGetCardNoResult(p0: String?) {

        }

        override fun onPinKey_TDES_Result(p0: String?) {

        }

        override fun onBatchReadMifareCardResult(
            p0: String?,
            p1: Hashtable<String, MutableList<String>>?
        ) {

        }

        override fun onReturnRSAResult(p0: String?) {

        }

        override fun onReturnUpdateIPEKResult(p0: Boolean) {

        }

        override fun onQposIdResult(p0: Hashtable<String, String>?) {

        }

        override fun onWaitingforData(p0: String?) {

        }

        override fun onQposDoGetTradeLogNum(p0: String?) {

        }

        override fun onSetParamsResult(p0: Boolean, p1: Hashtable<String, Any>?) {

        }

        override fun onOperateMifareCardResult(p0: Hashtable<String, String>?) {

        }

        override fun getMifareFastReadData(p0: Hashtable<String, String>?) {

        }

        override fun onSearchMifareCardResult(p0: Hashtable<String, String>?) {

        }

        override fun onRequestDevice() {

        }

        override fun onRequestUpdateKey(p0: String?) {

        }

        override fun onTradeCancelled() {

        }

        override fun onReturnCustomConfigResult(p0: Boolean, p1: String?) {

        }

        override fun getMifareCardVersion(p0: Hashtable<String, String>?) {

        }

        override fun onRequestSignatureResult(p0: ByteArray?) {

        }

        override fun onRequestSetPin() {
            LogUtils.d("onRequestSetPin")
            pos?.sendPin("123456")
        }

        override fun onBluetoothBonding() {

        }

        override fun onAddKey(p0: Boolean) {

        }

        override fun onRequestSetAmount() {
            Log.d("PosActivity","onRequestSetAmount")
            pos?.setAmount("25","10","166",QPOSService.TransactionType.GOODS)
        }

        override fun onGetShutDownTime(p0: String?) {

        }

        override fun onQposDoSetRsaPublicKey(p0: Boolean) {

        }

        override fun onReturnBatchSendAPDUResult(p0: LinkedHashMap<Int, String>?) {

        }

        override fun onReturnUpdateEMVResult(p0: Boolean) {

        }

        override fun onRequestDeviceScanFinished() {

        }

        override fun onQposGenerateSessionKeysResult(p0: Hashtable<String, String>?) {

        }

        override fun onRequestUpdateWorkKeyResult(p0: QPOSService.UpdateInformationResult?) {

        }

        override fun onQposDoGetTradeLog(p0: String?, p1: String?) {

        }

        override fun onGetInputAmountResult(p0: Boolean, p1: String?) {

        }

        override fun onGetBuzzerStatusResult(p0: String?) {

        }

        override fun onEmvICCExceptionData(p0: String?) {

        }

        override fun onReturnDownloadRsaPublicKey(p0: HashMap<String, String>?) {

        }

        override fun onReturnAESTransmissonKeyResult(p0: Boolean, p1: String?) {

        }

    }

}
