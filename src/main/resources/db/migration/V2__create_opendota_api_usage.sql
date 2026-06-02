CREATE TABLE opendota_api_usage (
                                    usage_date DATE PRIMARY KEY,

                                    request_count INT NOT NULL DEFAULT 0,

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT chk_opendota_api_usage_request_count_non_negative
                                        CHECK (request_count >= 0)
);