package com.trade.modal;

import com.trade.domain.VerificationType;
import jakarta.persistence.Embeddable;

@Embeddable
public class TwoFactorAuth {
    private boolean isEnabled = true;

    private VerificationType sendTo;

    // Getter for isEnabled
    public boolean isEnabled() {
        return isEnabled;
    }

    // Setter for isEnabled
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // Getter for sendTo
    public VerificationType getSendTo() {
        return sendTo;
    }

    // Setter for sendTo
    public void setSendTo(VerificationType sendTo) {
        this.sendTo = sendTo;
    }
}
