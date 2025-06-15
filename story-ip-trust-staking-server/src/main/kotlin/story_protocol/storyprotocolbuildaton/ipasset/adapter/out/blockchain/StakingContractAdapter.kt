package story_protocol.storyprotocolbuildaton.ipasset.adapter.out.blockchain

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import story_protocol.storyprotocolbuildaton.ipasset.application.port.out.StakingContractRepository
import story_protocol.storyprotocolbuildaton.ipasset.domain.SlashRecord
import java.math.BigInteger

@Component
class StakingContractAdapter(
    private val restTemplate: RestTemplate = RestTemplate(),
    private val objectMapper: ObjectMapper = ObjectMapper()
) : StakingContractRepository {

    private val logger = LoggerFactory.getLogger(StakingContractAdapter::class.java)

    @Value("\${blockchain.rpc.url}")
    private lateinit var rpcUrl: String

    @Value("\${staking.contract.address}")
    private lateinit var contractAddress: String

    override fun getTotalStakedAmounts(ipAssetIds: List<String>): Map<String, BigInteger> {
        if (ipAssetIds.isEmpty()) {
            return emptyMap()
        }
        
        return try {
            val functionSignature = "0xa38a7453" // getTotalStakedAmounts(address[]) function selector
            
            val arrayOffset = "0000000000000000000000000000000000000000000000000000000000000020"
            val arrayLength = ipAssetIds.size.toString(16).padStart(64, '0')
            val encodedAddresses = ipAssetIds.joinToString("") { ipAssetId ->
                ipAssetId.removePrefix("0x").lowercase().padStart(64, '0')
            }
            
            val data = functionSignature + arrayOffset + arrayLength + encodedAddresses

            val request = JsonRpcRequest(
                method = "eth_call",
                params = listOf(
                    mapOf("to" to contractAddress, "data" to data),
                    "latest"
                ),
                id = 1,
                jsonrpc = "2.0"
            )

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            val entity = HttpEntity(objectMapper.writeValueAsString(request), headers)
            val response = restTemplate.exchange(rpcUrl, HttpMethod.POST, entity, String::class.java)

            val jsonResponse = objectMapper.readTree(response.body)
            val result = jsonResponse.get("result")?.asText()

            if (result != null && result != "0x" && result != "0x0") {
                parseUint256Array(result, ipAssetIds)
            } else {
                ipAssetIds.associateWith { BigInteger.ZERO }
            }
        } catch (e: Exception) {
            ipAssetIds.associateWith { BigInteger.ZERO }
        }
    }

    override fun getUserStakedAmounts(userAddress: String, ipAssetIds: List<String>): Map<String, BigInteger> {
        if (ipAssetIds.isEmpty()) {
            return emptyMap()
        }
        
        return try {
            val functionSignature = "0x8efb66f9" // getUserStakedAmounts(address,address[]) function selector
            
            val userAddressParam = userAddress.removePrefix("0x").lowercase().padStart(64, '0')
            val arrayOffset = "0000000000000000000000000000000000000000000000000000000000000040"
            val arrayLength = ipAssetIds.size.toString(16).padStart(64, '0')
            val encodedAddresses = ipAssetIds.joinToString("") { ipAssetId ->
                ipAssetId.removePrefix("0x").lowercase().padStart(64, '0')
            }
            
            val data = functionSignature + userAddressParam + arrayOffset + arrayLength + encodedAddresses

            val request = JsonRpcRequest(
                method = "eth_call",
                params = listOf(
                    mapOf("to" to contractAddress, "data" to data),
                    "latest"
                ),
                id = 1,
                jsonrpc = "2.0"
            )

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            val entity = HttpEntity(objectMapper.writeValueAsString(request), headers)
            val response = restTemplate.exchange(rpcUrl, HttpMethod.POST, entity, String::class.java)

            val jsonResponse = objectMapper.readTree(response.body)
            val result = jsonResponse.get("result")?.asText()

            if (result != null && result != "0x" && result != "0x0") {
                parseUint256Array(result, ipAssetIds)
            } else {
                ipAssetIds.associateWith { BigInteger.ZERO }
            }
        } catch (e: Exception) {
            ipAssetIds.associateWith { BigInteger.ZERO }
        }
    }

    override fun getIPAssetsSlashHistory(ipAssetIds: List<String>): Map<String, List<SlashRecord>> {
        if (ipAssetIds.isEmpty()) {
            return emptyMap()
        }
        
        return try {
            val functionSignature = "0x70441d58" // getIPAssetsSlashHistory(address[]) function selector
            
            val arrayOffset = "0000000000000000000000000000000000000000000000000000000000000020"
            val arrayLength = ipAssetIds.size.toString(16).padStart(64, '0')
            val encodedAddresses = ipAssetIds.joinToString("") { ipAssetId ->
                ipAssetId.removePrefix("0x").lowercase().padStart(64, '0')
            }
            
            val data = functionSignature + arrayOffset + arrayLength + encodedAddresses

            val request = JsonRpcRequest(
                method = "eth_call",
                params = listOf(
                    mapOf("to" to contractAddress, "data" to data),
                    "latest"
                ),
                id = 1,
                jsonrpc = "2.0"
            )

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON

            val entity = HttpEntity(objectMapper.writeValueAsString(request), headers)
            val response = restTemplate.exchange(rpcUrl, HttpMethod.POST, entity, String::class.java)

            val jsonResponse = objectMapper.readTree(response.body)
            val result = jsonResponse.get("result")?.asText()

            if (result != null && result != "0x" && result != "0x0") {
                parseSlashHistoryArray(result, ipAssetIds)
            } else {
                ipAssetIds.associateWith { emptyList<SlashRecord>() }
            }
        } catch (e: Exception) {
            logger.error("Error calling getIPAssetsSlashHistory: ${e.message}", e)
            ipAssetIds.associateWith { emptyList<SlashRecord>() }
        }
    }

    private fun parseUint256Array(hexResult: String, ipAssetIds: List<String>): Map<String, BigInteger> {
        val result = hexResult.removePrefix("0x")
        
        val arrayLengthHex = result.substring(64, 128)
        val arrayLength = BigInteger(arrayLengthHex, 16).toInt()
        
        if (arrayLength != ipAssetIds.size) {
            return ipAssetIds.associateWith { BigInteger.ZERO }
        }
        
        val resultMap = mutableMapOf<String, BigInteger>()
        
        for (i in 0 until arrayLength) {
            val startIndex = 128 + (i * 64)
            val endIndex = startIndex + 64
            
            if (endIndex <= result.length) {
                val valueHex = result.substring(startIndex, endIndex)
                val value = if (valueHex.all { it == '0' }) {
                    BigInteger.ZERO
                } else {
                    BigInteger(valueHex, 16)
                }
                resultMap[ipAssetIds[i]] = value
            } else {
                resultMap[ipAssetIds[i]] = BigInteger.ZERO
            }
        }
        
        return resultMap
    }

    private fun parseSlashHistoryArray(hexResult: String, ipAssetIds: List<String>): Map<String, List<SlashRecord>> {
        val result = hexResult.removePrefix("0x")
        
        try {
            // First 64 bytes: offset to outer array (0x20 = 32)
            val outerArrayOffset = 64
            
            // Second 64 bytes: length of outer array
            val outerArrayLengthHex = result.substring(outerArrayOffset, outerArrayOffset + 64)
            val outerArrayLength = BigInteger(outerArrayLengthHex, 16).toInt()
            
            if (outerArrayLength != ipAssetIds.size) {
                logger.warn("Array length mismatch: got $outerArrayLength, expected ${ipAssetIds.size}")
                return ipAssetIds.associateWith { emptyList<SlashRecord>() }
            }
            
            val resultMap = mutableMapOf<String, List<SlashRecord>>()
            
            for (i in 0 until outerArrayLength) {
                // Third 64 bytes: offset to inner array
                val innerArrayOffsetHex = result.substring(128 + (i * 64), 128 + (i * 64) + 64)
                val innerArrayOffset = BigInteger(innerArrayOffsetHex, 16).toInt() * 2

                // Calculate absolute offset for inner array
                val absoluteInnerArrayOffset = 128 + innerArrayOffset
                val innerArrayLengthHex = result.substring(absoluteInnerArrayOffset, absoluteInnerArrayOffset + 64)
                val innerArrayLength = BigInteger(innerArrayLengthHex, 16).toInt()
                
                val slashRecords = mutableListOf<SlashRecord>()

                val recordCount = innerArrayLength

                // Start reading data after array length
                var dataOffset = absoluteInnerArrayOffset + 64
                
                for (j in 0 until recordCount) {
                    if (dataOffset + 256 <= result.length) {
                        val slashedIPAssetHex = result.substring(dataOffset + 24, dataOffset + 64)
                        val redistributedToIPAssetHex = result.substring(dataOffset + 88, dataOffset + 128)
                        val amountHex = result.substring(dataOffset + 128, dataOffset + 192)
                        val timestampHex = result.substring(dataOffset + 192, dataOffset + 256)
                        
                        val slashedIPAsset = "0x" + slashedIPAssetHex
                        val redistributedToIPAsset = "0x" + redistributedToIPAssetHex
                        val amount = if (amountHex.all { it == '0' }) BigInteger.ZERO else BigInteger(amountHex, 16)
                        val timestamp = if (timestampHex.all { it == '0' }) 0L else BigInteger(timestampHex, 16).toLong()
                        
                        slashRecords.add(SlashRecord(slashedIPAsset, redistributedToIPAsset, amount, timestamp))

                        dataOffset += 256
                    } else {
                        logger.warn("Not enough data for record $j at offset $dataOffset")
                        break
                    }
                }
                
                resultMap[ipAssetIds[i]] = slashRecords
            }
            
            return resultMap
        } catch (e: Exception) {
            logger.error("Error parsing slash history array: ${e.message}", e)
            return ipAssetIds.associateWith { emptyList<SlashRecord>() }
        }
    }

    data class JsonRpcRequest(
        val method: String,
        val params: List<Any>,
        val id: Int,
        val jsonrpc: String
    )
}
