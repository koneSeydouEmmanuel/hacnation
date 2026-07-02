CREATE TABLE IF NOT EXISTS reporting_admin_db.piste_audit (
    id          VARCHAR(36)    NOT NULL PRIMARY KEY,
    user_id     VARCHAR(255)   NOT NULL,
    timestamp   TIMESTAMP      NOT NULL DEFAULT NOW(),
    service     VARCHAR(100)   NOT NULL,
    action      VARCHAR(100)   NOT NULL,
    old_value   TEXT,
    new_value   TEXT,
    entity_id   VARCHAR(255)   NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_piste_audit_user_id ON reporting_admin_db.piste_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_piste_audit_action ON reporting_admin_db.piste_audit(action);
CREATE INDEX IF NOT EXISTS idx_piste_audit_timestamp ON reporting_admin_db.piste_audit(timestamp);
CREATE INDEX IF NOT EXISTS idx_piste_audit_service ON reporting_admin_db.piste_audit(service);
