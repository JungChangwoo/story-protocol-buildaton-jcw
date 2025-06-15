package story_protocol.storyprotocolbuildaton.slashhistory.adapter.out.blockchain

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import story_protocol.storyprotocolbuildaton.slashhistory.application.port.out.SlashHistoryRepository
import story_protocol.storyprotocolbuildaton.slashhistory.domain.PaginatedSlashHistory
import story_protocol.storyprotocolbuildaton.slashhistory.domain.SlashRecord
import java.math.BigInteger
import java.time.Instant

@Component
class SlashHistoryBlockchainAdapter(
    private val restTemplate: RestTemplate = RestTemplate(),
    private val objectMapper: ObjectMapper = ObjectMapper()
) : SlashHistoryRepository {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${blockchain.rpc.url}")
    private lateinit var rpcUrl: String

    @Value("\${staking.contract.address}")
    private lateinit var contractAddress: String

    override fun getSlashHistoryPaginated(offset: Long, limit: Long): PaginatedSlashHistory {
        return try {
            val functionSignature = "0x89b6a742"
            
            val offsetParam = offset.toString(16).padStart(64, '0')
            val limitParam = limit.toString(16).padStart(64, '0')
            
            val data = functionSignature + offsetParam + limitParam

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
                parseSlashHistoryPaginated(result, offset, limit)
            } else {
                PaginatedSlashHistory(emptyList(), 0L, offset, limit)
            }
        } catch (e: Exception) {
            PaginatedSlashHistory(emptyList(), 0L, offset, limit)
        }
    }

    private fun parseSlashHistoryPaginated(hexResult: String, offset: Long, limit: Long): PaginatedSlashHistory {
        val result = hexResult.removePrefix("0x")
        
        return try {
            val recordsArrayOffset = BigInteger(result.substring(0, 64), 16).toInt() * 2
            val totalCount = BigInteger(result.substring(64, 128), 16).toLong()
            
            val recordsArrayLength = BigInteger(result.substring(recordsArrayOffset, recordsArrayOffset + 64), 16).toInt()
            
            val records = mutableListOf<SlashRecord>()
            
            for (i in 0 until recordsArrayLength) {
                val recordStart = recordsArrayOffset + 64 + (i * 256)
                
                if (recordStart + 256 <= result.length) {
                    val slashedIPAssetHex = result.substring(recordStart + 24, recordStart + 64)
                    val redistributedToIPAssetHex = result.substring(recordStart + 88, recordStart + 128)
                    val amountHex = result.substring(recordStart + 128, recordStart + 192)
                    val timestampHex = result.substring(recordStart + 192, recordStart + 256)
                    
                    val slashedIPAsset = "0x" + slashedIPAssetHex
                    val redistributedToIPAsset = "0x" + redistributedToIPAssetHex
                    val amount = if (amountHex.all { it == '0' }) "0" else BigInteger(amountHex, 16).toString()
                    val timestamp = if (timestampHex.all { it == '0' }) 0L else BigInteger(timestampHex, 16).toLong()
                    
                    records.add(SlashRecord(
                        slashedIPAsset,
                        redistributedToIPAsset,
                        amount,
                        Instant.ofEpochSecond(timestamp)
                    ))
                }
            }
            
            PaginatedSlashHistory(records, totalCount, offset, limit)
        } catch (e: Exception) {
            PaginatedSlashHistory(emptyList(), 0L, offset, limit)
        }
    }

    data class JsonRpcRequest(
        val method: String,
        val params: List<Any>,
        val id: Int,
        val jsonrpc: String
    )
}
