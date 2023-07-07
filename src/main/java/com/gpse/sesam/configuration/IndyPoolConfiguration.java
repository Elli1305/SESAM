package com.gpse.sesam.configuration;

import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.LibIndy;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.pool.PoolJSONParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

@Configuration
public class IndyPoolConfiguration {
    private static final String DEFAULT_POOL_NAME = "default_pool";

    @Bean
    public Pool pool() throws FileNotFoundException, IndyException, ExecutionException, InterruptedException {
        LibIndy.init();

        if (LibIndy.api == null) {
            return null;
        }

        File genesisTxnFile = ResourceUtils.getFile("classpath:test.bcovrin.vonx.io.jsonl");

        PoolJSONParameters.CreatePoolLedgerConfigJSONParameter createPoolLedgerConfigJSONParameter =
                new PoolJSONParameters.CreatePoolLedgerConfigJSONParameter(genesisTxnFile.getAbsolutePath());

        Pool.setProtocolVersion(2).get();

        try {
            Pool.createPoolLedgerConfig(DEFAULT_POOL_NAME, createPoolLedgerConfigJSONParameter.toJson()).get();
        } catch (ExecutionException | IndyException | InterruptedException ignored) {
            // Intentionally ignored.
        }

        return Pool.openPoolLedger(DEFAULT_POOL_NAME, "{}").get();
    }
}
